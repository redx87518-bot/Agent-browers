# AI Agent Browser

A production-oriented native Android browser built on **Mozilla GeckoView** with a custom **Pose 4 — Light Clean** interface. This project provides a genuine mobile browsing foundation designed for future AI-agent integration.

## Features

### Real Browser Engine
- **Mozilla GeckoView** as the web rendering engine
- Real HTTP/HTTPS website loading
- JavaScript-capable modern web pages
- Redirect handling and normal webpage interaction
- Page title and URL updates from actual navigation

### Tab Management
- Create real independent browser tabs backed by Gecko sessions
- Switch between tabs with preserved session state
- Close individual tabs or all tabs
- Track active tab and last-active timestamps
- Memory-efficient tab lifecycle management

### Navigation
- Back and forward navigation tied to actual session history
- Refresh and stop loading controls
- Loading progress based on real engine callbacks
- URL and title updates from genuine navigation events

### Address & Search
- Modern address and search bar
- Direct URL loading for complete URLs
- Domain-style address resolution with HTTPS
- Web search using configurable search provider
- Keyboard submission support

### History Foundation
- Real browsing history from actual navigation
- History entries with URL, title, timestamp, and favicon
- Room database persistence for history and tabs
- Foundation for future history screen

### Download Handling
- Real download foundation through Android DownloadManager
- Download status tracking
- No fake or simulated downloads

### Architecture
- Clean layered architecture: Presentation → Domain → Data → Platform
- Gecko engine isolated behind clear interfaces
- ViewModel-based state management
- Jetpack Compose UI with Material 3
- Room persistence for tabs and history
- Single source of truth for browser state

### Design
- **Pose 4 — Light Clean** visual direction
- Bright light backgrounds with white surfaces
- Modern blue accent color
- Soft rounded corners and subtle shadows
- Clean typography with comfortable spacing
- Mobile-first touch-optimized controls

## Project Structure

```
app/src/main/java/dev/agentbrowser/
├── BrowserApplication.kt          # Application class, DI wiring
├── MainActivity.kt                # Launcher activity
├── domain/
│   ├── model/
│   │   ├── Tab.kt                 # Tab data model
│   │   ├── BrowserState.kt        # Browser state model
│   │   ├── HistoryEntry.kt        # History entry model
│   │   └── SearchProvider.kt      # Search provider interface + Google impl
│   ├── repository/
│   │   ├── BrowserRepository.kt   # Browser repository interface
│   │   ├── TabManager.kt          # Tab manager interface
│   │   ├── HistoryRepository.kt   # History repository interface
│   │   └── DownloadRepository.kt  # Download repository interface
│   └── usecase/
│       └── BrowserActions.kt      # Future AI agent action interfaces
├── data/
│   ├── local/
│   │   ├── BrowserDatabase.kt     # Room database
│   │   ├── entity/
│   │   │   ├── TabEntity.kt       # Tab Room entity
│   │   │   └── HistoryEntryEntity.kt
│   │   ├── dao/
│   │   │   ├── TabDao.kt          # Tab DAO
│   │   │   └── HistoryDao.kt      # History DAO
│   │   └── mapper/
│   │       ├── TabMapper.kt       # Entity ↔ Domain mapper
│   │       └── HistoryEntryMapper.kt
│   └── repository/
│       ├── BrowserRepositoryImpl.kt
│       ├── TabManagerImpl.kt
│       ├── HistoryRepositoryImpl.kt
│       └── DownloadRepositoryImpl.kt
├── platform/
│   ├── GeckoEngine.kt             # GeckoView engine wrapper
│   ├── PermissionHelper.kt        # Android permission helper
│   ├── DownloadHandler.kt         # Download handler interface
│   └── SecureStorage.kt           # Encrypted storage
└── presentation/
    ├── viewmodel/
    │   ├── BrowserViewModel.kt    # Browser screen ViewModel
    │   └── TabsViewModel.kt       # Tabs screen ViewModel
    ├── screens/
    │   ├── BrowserScreen.kt       # Main browser UI
    │   ├── TabsScreen.kt          # Tab management UI
    │   ├── NewTabPage.kt          # New tab page
    │   └── PlaceholderScreen.kt   # Future feature placeholders
    ├── components/
    │   ├── AddressBar.kt          # URL/search input
    │   ├── BrowserScaffold.kt     # Browser layout scaffold
    │   └── BottomNav.kt           # Bottom navigation
    └── navigation/
        └── NavGraph.kt            # Navigation graph
```

## Technology Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Engine:** Mozilla GeckoView 125
- **Persistence:** Room
- **Async:** Kotlin Coroutines + Flow
- **Architecture:** Clean Architecture / MVVM

## Building

### Prerequisites

- JDK 17
- Android SDK with:
  - Android SDK Platform 34
  - Android SDK Build-Tools 34
  - Android SDK Platform-Tools

### Gradle Build

```bash
./gradlew assembleDebug
```

Or using the locally downloaded Gradle:

```bash
gradle-8.4/bin/gradle assembleDebug
```

### Release Build

```bash
./gradlew assembleRelease
```

## Future AI Integration

This project is architected as a foundation for a future AI Agent Browser. Phase 1 establishes:

- Real browser action interfaces in `domain/usecase/BrowserActions.kt`
- Controlled browser boundaries for future agent access
- No fake AI implementations in Phase 1

Future phases may add:
- AI-driven browsing automation
- Gmail and contacts integration
- Task execution
- WebExtensions for controlled page inspection

## Development Rules

- **No fake implementations** — every feature must genuinely work or be marked unavailable
- **GeckoView only** — do not build a browser engine from scratch
- **One feature group at a time** — implement and verify incrementally
- **Build after changes** — fix compilation errors before moving on
- **Read before modifying** — inspect existing code before rewriting

## License

Proprietary — AI Agent Browser
