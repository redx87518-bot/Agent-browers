package dev.agentbrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntryEntity(
    @PrimaryKey val id: String,
    val url: String,
    val title: String,
    val timestamp: Long,
    val faviconUrl: String?
)
