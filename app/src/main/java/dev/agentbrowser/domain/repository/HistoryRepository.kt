package dev.agentbrowser.domain.repository

import dev.agentbrowser.domain.model.HistoryEntry
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getRecentHistory(limit: Int = 50): Flow<List<HistoryEntry>>
    suspend fun addEntry(entry: HistoryEntry)
    suspend fun clearHistory()
}
