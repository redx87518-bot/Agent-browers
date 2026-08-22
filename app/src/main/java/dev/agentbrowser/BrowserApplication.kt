package dev.agentbrowser

import android.app.Application
import androidx.room.Room
import dev.agentbrowser.data.local.BrowserDatabase
import dev.agentbrowser.data.local.dao.HistoryDao
import dev.agentbrowser.data.local.dao.TabDao
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
import dev.agentbrowser.platform.GeckoEngine
import dev.agentbrowser.platform.PermissionHelper
import dev.agentbrowser.platform.SecureStorage

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
    lateinit var secureStorage: SecureStorage
        private set

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(this, BrowserDatabase::class.java, "browser-db").build()
        val tabDao = database.tabDao()
        val historyDao = database.historyDao()

        permissionHelper = PermissionHelper(this)
        secureStorage = SecureStorage(this)
        val downloadHandler = AndroidDownloadHandler(this)
        val engine = GeckoEngine(this)
        historyRepository = HistoryRepositoryImpl(historyDao)
        tabManager = TabManagerImpl(tabDao, engine)
        downloadRepository = DownloadRepositoryImpl(downloadHandler)
        browserRepository = BrowserRepositoryImpl(engine, tabManager, historyRepository)
    }
}
