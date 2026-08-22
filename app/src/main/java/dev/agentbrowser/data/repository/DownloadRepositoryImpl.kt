package dev.agentbrowser.data.repository

import dev.agentbrowser.domain.repository.DownloadRepository
import dev.agentbrowser.platform.DownloadHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DownloadRepositoryImpl(
    private val downloadHandler: DownloadHandler
) : DownloadRepository {

    private val _activeDownloads = MutableStateFlow<List<DownloadItem>>(emptyList())
    override val activeDownloads: Flow<List<DownloadItem>> = _activeDownloads.asStateFlow()

    override suspend fun startDownload(url: String, mimeType: String?, userAgent: String?) {
        val id = downloadHandler.enqueueDownload(url, mimeType, userAgent)
        val item = DownloadItem(
            id = id,
            url = url,
            mimeType = mimeType,
            status = DownloadStatus.PENDING
        )
        _activeDownloads.update { it + item }
    }
}
