package dev.agentbrowser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TabEntity::class, HistoryEntryEntity::class], version = 1, exportSchema = false)
abstract class BrowserDatabase : RoomDatabase() {
    abstract fun tabDao(): TabDao
    abstract fun historyDao(): HistoryDao
}
