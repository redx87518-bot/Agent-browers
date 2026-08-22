// AI Agent Browser - HTML UI Controller
// Communicates with native Kotlin layer via BrowserBridge

class BrowserUI {
    constructor() {
        this.currentTabId = null;
        this.tabs = [];
        this.isLoading = false;
        this.bridge = null;
        
        this.init();
    }

    init() {
        this.setupWebView();
        this.bindEvents();
        this.showScreen('newtab');
        this.pollForBridge();
    }

    pollForBridge() {
        const check = () => {
            if (window.BrowserBridge) {
                this.bridge = window.BrowserBridge;
            } else {
                setTimeout(check, 50);
            }
        };
        check();
    }

    setupWebView() {
        const webview = document.getElementById('main-webview');
        if (!webview) return;

        webview.addEventListener('did-start-loading', () => {
            this.setLoading(true);
        });

        webview.addEventListener('did-stop-loading', () => {
            this.setLoading(false);
        });

        webview.addEventListener('did-fail-load', (event) => {
            if (event.isMainFrame) {
                this.showError(event.errorDescription || 'Unknown error');
            }
        });

        webview.addEventListener('page-title-updated', (event) => {
            this.updateTabTitle(this.currentTabId, event.title);
        });

        webview.addEventListener('did-navigate', (event) => {
            this.updateTabUrl(this.currentTabId, event.url);
            this.updateUrlInput(event.url);
        });

        webview.addEventListener('did-navigate-in-page', (event) => {
            this.updateTabUrl(this.currentTabId, event.url);
            this.updateUrlInput(event.url);
        });

        webview.addEventListener('load-progress-changed', (event) => {
            this.updateLoadingProgress(event.progress);
        });
    }

    bindEvents() {
        const urlInput = document.getElementById('url-input');
        if (urlInput) {
            urlInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter' && urlInput.value.trim()) {
                    this.navigateTo(urlInput.value.trim());
                }
            });
        }

        const clearBtn = document.getElementById('clear-btn');
        if (clearBtn) {
            clearBtn.addEventListener('click', () => {
                const urlInput = document.getElementById('url-input');
                if (urlInput) {
                    urlInput.value = '';
                    urlInput.focus();
                    this.updateClearButton('');
                }
            });
        }

        const newtabInput = document.getElementById('newtab-input');
        if (newtabInput) {
            newtabInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter' && newtabInput.value.trim()) {
                    this.navigateTo(newtabInput.value.trim());
                }
            });
        }

        const tabsBtn = document.getElementById('tabs-btn');
        if (tabsBtn) {
            tabsBtn.addEventListener('click', () => {
                this.showScreen('tabs');
            });
        }

        const dockTabsBtn = document.getElementById('dock-tabs-btn');
        if (dockTabsBtn) {
            dockTabsBtn.addEventListener('click', () => {
                this.showScreen('tabs');
            });
        }

        const backBtn = document.getElementById('back-btn');
        if (backBtn) {
            backBtn.addEventListener('click', () => {
                this.goBack();
            });
        }

        const forwardBtn = document.getElementById('forward-btn');
        if (forwardBtn) {
            forwardBtn.addEventListener('click', () => {
                this.goForward();
            });
        }

        const reloadBtn = document.getElementById('reload-btn');
        if (reloadBtn) {
            reloadBtn.addEventListener('click', () => {
                this.reload();
            });
        }

        const newTabBtn = document.getElementById('new-tab-btn');
        if (newTabBtn) {
            newTabBtn.addEventListener('click', () => {
                this.createNewTab();
            });
        }

        const retryBtn = document.getElementById('retry-btn');
        if (retryBtn) {
            retryBtn.addEventListener('click', () => {
                this.retry();
            });
        }

        document.querySelectorAll('.quick-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                const url = btn.dataset.url;
                if (url) {
                    this.navigateTo(url);
                }
            });
        });
    }

    callNative(action, data) {
        if (this.bridge && typeof this.bridge[action] === 'function') {
            if (data !== undefined && data !== null) {
                this.bridge[action](data);
            } else {
                this.bridge[action]();
            }
        }
    }

    navigateTo(url) {
        this.hideError();
        this.callNative('navigateTo', url);
        this.showScreen('browser');
    }

    goBack() {
        this.callNative('goBack');
    }

    goForward() {
        this.callNative('goForward');
    }

    reload() {
        this.callNative('reload');
    }

    stopLoading() {
        this.callNative('stopLoading');
    }

    createNewTab() {
        this.callNative('createNewTab');
        this.showScreen('newtab');
    }

    closeTab(tabId) {
        this.callNative('closeTab', tabId);
    }

    switchTab(tabId) {
        this.callNative('switchTab', tabId);
        this.showScreen('browser');
    }

    retry() {
        this.hideError();
        this.callNative('retry');
    }

    setLoading(loading) {
        this.isLoading = loading;
        const loadingBar = document.getElementById('loading-bar');
        if (loadingBar) {
            if (loading) {
                loadingBar.style.width = '70%';
                loadingBar.style.opacity = '1';
            } else {
                loadingBar.style.width = '100%';
                setTimeout(() => {
                    loadingBar.style.width = '0%';
                    loadingBar.style.opacity = '0';
                }, 200);
            }
        }
    }

    updateLoadingProgress(progress) {
        const loadingBar = document.getElementById('loading-bar');
        if (loadingBar) {
            const clampedProgress = Math.max(0, Math.min(100, progress));
            loadingBar.style.width = clampedProgress + '%';
            loadingBar.style.opacity = clampedProgress < 100 ? '1' : '0';
        }
    }

    updateUrlInput(url) {
        const urlInput = document.getElementById('url-input');
        if (urlInput && url) {
            urlInput.value = url;
            this.updateClearButton(url);
        }
    }

    updateClearButton(url) {
        const clearBtn = document.getElementById('clear-btn');
        if (clearBtn) {
            clearBtn.style.display = url && url.length > 0 ? 'flex' : 'none';
        }
    }

    updateTabTitle(tabId, title) {
        const tab = this.tabs.find(t => t.id === tabId);
        if (tab) {
            tab.title = title;
            this.renderTabs();
        }
    }

    updateTabUrl(tabId, url) {
        const tab = this.tabs.find(t => t.id === tabId);
        if (tab) {
            tab.url = url;
            this.renderTabs();
        }
    }

    setTabs(tabs, activeTabId) {
        this.tabs = tabs;
        this.currentTabId = activeTabId;
        this.renderTabs();
        
        const tabCount = document.getElementById('tab-count');
        if (tabCount) {
            tabCount.textContent = tabs.length;
        }
    }

    renderTabs() {
        const tabsList = document.getElementById('tabs-list');
        if (!tabsList) return;

        tabsList.innerHTML = '';

        this.tabs.forEach(tab => {
            const tabCard = document.createElement('div');
            tabCard.className = 'tab-card' + (tab.id === this.currentTabId ? ' active' : '');
            tabCard.innerHTML = `
                <div class="tab-favicon">${tab.title.charAt(0).toUpperCase()}</div>
                <div class="tab-info">
                    <div class="tab-title">${tab.title || 'New Tab'}</div>
                    <div class="tab-url">${tab.url || 'about:blank'}</div>
                </div>
                <button class="tab-close" data-tab-id="${tab.id}">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                </button>
            `;

            tabCard.addEventListener('click', (e) => {
                if (!e.target.closest('.tab-close')) {
                    this.switchTab(tab.id);
                }
            });

            const closeBtn = tabCard.querySelector('.tab-close');
            if (closeBtn) {
                closeBtn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    this.closeTab(tab.id);
                });
            }

            tabsList.appendChild(tabCard);
        });
    }

    updateNavigationState(canGoBack, canGoForward) {
        const backBtn = document.getElementById('back-btn');
        const forwardBtn = document.getElementById('forward-btn');
        
        if (backBtn) {
            backBtn.disabled = !canGoBack;
        }
        if (forwardBtn) {
            forwardBtn.disabled = !canGoForward;
        }
    }

    showError(message) {
        const errorScreen = document.getElementById('error-screen');
        const errorMessage = document.getElementById('error-message');
        if (errorScreen) {
            errorScreen.classList.add('active');
        }
        if (errorMessage && message) {
            errorMessage.textContent = message;
        }
    }

    hideError() {
        const errorScreen = document.getElementById('error-screen');
        if (errorScreen) {
            errorScreen.classList.remove('active');
        }
    }

    showScreen(screenName) {
        document.querySelectorAll('.screen').forEach(screen => {
            screen.classList.remove('active');
        });

        const screenMap = {
            'browser': 'browser-screen',
            'newtab': 'newtab-screen',
            'tabs': 'tabs-screen',
            'error': 'error-screen'
        };

        const screenId = screenMap[screenName];
        if (screenId) {
            const screen = document.getElementById(screenId);
            if (screen) {
                screen.classList.add('active');
            }
        }
    }

    showTabs() {
        this.showScreen('tabs');
    }

    showBrowser() {
        this.showScreen('browser');
    }

    showNewTab() {
        this.showScreen('newtab');
    }
}

// Initialize
const browserUI = new BrowserUI();
