package sl.kacinz.onluanmer.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import sl.kacinz.onluanmer.data.local.dao.GoalDao
import sl.kacinz.onluanmer.data.local.dao.TransactionDao
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.model.Transaction

@Database(entities = [Goal::class, Transaction::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun transactionDao(): TransactionDao
}
