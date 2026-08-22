package dev.agentbrowser.data.local.mapper

import dev.agentbrowser.domain.model.Tab

object TabMapper {
    fun toEntity(tab: Tab): TabEntity = TabEntity(
        id = tab.id,
        url = tab.url,
        title = tab.title,
        faviconUrl = tab.faviconUrl,
        createdAt = tab.createdAt,
        lastActiveAt = tab.lastActiveAt
    )

    fun toDomain(entity: TabEntity): Tab = Tab(
        id = entity.id,
        url = entity.url,
        title = entity.title,
        faviconUrl = entity.faviconUrl,
        createdAt = entity.createdAt,
        lastActiveAt = entity.lastActiveAt
    )
}
