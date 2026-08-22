package dev.agentbrowser.platform

import android.content.Context
import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface DownloadHandler {
    fun enqueueDownload(url: String, mimeType: String?, userAgent: String?): String
}

class AndroidDownloadHandler(private val context: Context) : DownloadHandler {
    override fun enqueueDownload(url: String, mimeType: String?, userAgent: String?): String {
        val request = DownloadManager.Request(Uri.parse(url)).apply {
            setMimeType(mimeType ?: "application/octet-stream")
            addRequestHeader("User-Agent", userAgent ?: "")
            setTitle("Downloading...")
            setDescription(url)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url.substringAfterLast("/"))
        }
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val id = downloadManager.enqueue(request)
        return id.toString()
    }
}
