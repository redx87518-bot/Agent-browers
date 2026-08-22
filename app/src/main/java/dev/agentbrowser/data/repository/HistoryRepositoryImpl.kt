package dev.agentbrowser.data.repository

import dev.agentbrowser.domain.model.HistoryEntry
import dev.agentbrowser.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class HistoryRepositoryImpl : HistoryRepository {

    private val _history = MutableStateFlow<List<HistoryEntry>>(emptyList())

    override fun getRecentHistory(limit: Int): Flow<List<HistoryEntry>> {
        return _history.map { it.takeLast(limit) }
    }

    override suspend fun addEntry(entry: HistoryEntry) {
        _history.update { it + entry }
    }

    override suspend fun clearHistory() {
        _history.value = emptyList()
    }
}
