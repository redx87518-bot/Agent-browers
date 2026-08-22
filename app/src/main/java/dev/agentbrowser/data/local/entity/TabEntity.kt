package dev.agentbrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabs")
data class TabEntity(
    @PrimaryKey val id: String,
    val url: String,
    val title: String,
    val faviconUrl: String?,
    val createdAt: Long,
    val lastActiveAt: Long
)
