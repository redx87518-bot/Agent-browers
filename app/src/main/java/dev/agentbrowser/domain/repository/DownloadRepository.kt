package dev.agentbrowser.domain.repository

import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getActiveDownloads(): Flow<List<DownloadItem>>
    suspend fun startDownload(url: String, mimeType: String?, userAgent: String?)
}

data class DownloadItem(
    val id: String,
    val url: String,
    val mimeType: String?,
    val status: DownloadStatus,
    val localUri: String? = null
)

enum class DownloadStatus { PENDING, DOWNLOADING, COMPLETED, FAILED, CANCELLED }
