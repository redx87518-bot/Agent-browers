package dev.agentbrowser.data.repository

import dev.agentbrowser.data.local.dao.HistoryDao
import dev.agentbrowser.data.local.mapper.HistoryEntryMapper
import dev.agentbrowser.domain.model.HistoryEntry
import dev.agentbrowser.domain.repository.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getRecentHistory(limit: Int): Flow<List<HistoryEntry>> {
        return historyDao.getRecentHistory(limit).map { entities ->
            entities.map { HistoryEntryMapper.toDomain(it) }
        }
    }

    override suspend fun addEntry(entry: HistoryEntry) {
        historyDao.insertEntry(HistoryEntryMapper.toEntity(entry))
    }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}
