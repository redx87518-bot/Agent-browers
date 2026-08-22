package dev.agentbrowser.data.local.mapper

import dev.agentbrowser.domain.model.HistoryEntry

object HistoryEntryMapper {
    fun toEntity(entry: HistoryEntry): HistoryEntryEntity = HistoryEntryEntity(
        id = entry.id,
        url = entry.url,
        title = entry.title,
        timestamp = entry.timestamp,
        faviconUrl = entry.faviconUrl
    )

    fun toDomain(entity: HistoryEntryEntity): HistoryEntry = HistoryEntry(
        id = entity.id,
        url = entity.url,
        title = entity.title,
        timestamp = entity.timestamp,
        faviconUrl = entity.faviconUrl
    )
}
