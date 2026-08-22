package dev.agentbrowser.domain.model

import java.util.UUID

data class Tab(
    val id: String = UUID.randomUUID().toString(),
    val url: String = "",
    val title: String = "",
    val faviconUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActiveAt: Long = System.currentTimeMillis()
)
