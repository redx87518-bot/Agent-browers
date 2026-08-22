package dev.agentbrowser.domain.model

import java.util.UUID

data class HistoryEntry(
    val id: String = UUID.randomUUID().toString(),
    val url: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val faviconUrl: String? = null
)
