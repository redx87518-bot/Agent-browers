package dev.agentbrowser

import android.app.Application
import dev.agentbrowser.data.repository.BrowserRepositoryImpl
import dev.agentbrowser.data.repository.DownloadRepositoryImpl
import dev.agentbrowser.data.repository.HistoryRepositoryImpl
import dev.agentbrowser.data.repository.TabManagerImpl
import dev.agentbrowser.domain.repository.BrowserRepository
import dev.agentbrowser.domain.repository.DownloadRepository
import dev.agentbrowser.domain.repository.HistoryRepository
import dev.agentbrowser.domain.repository.TabManager
import dev.agentbrowser.platform.AndroidDownloadHandler
import dev.agentbrowser.platform.DownloadHandler
import dev.agentbrowser.platform.WebViewEngine
import dev.agentbrowser.platform.PermissionHelper

class BrowserApplication : Application() {
    lateinit var browserRepository: BrowserRepository
        private set
    lateinit var tabManager: TabManager
        private set
    lateinit var historyRepository: HistoryRepository
        private set
    lateinit var downloadRepository: DownloadRepository
        private set
    lateinit var permissionHelper: PermissionHelper
        private set

    override fun onCreate() {
        super.onCreate()
        permissionHelper = PermissionHelper(this)
        val downloadHandler = AndroidDownloadHandler(this)
        val engine = WebViewEngine(this)
        historyRepository = HistoryRepositoryImpl()
        tabManager = TabManagerImpl(engine)
        downloadRepository = DownloadRepositoryImpl(downloadHandler)
        browserRepository = BrowserRepositoryImpl(engine, tabManager, historyRepository)
    }
}
