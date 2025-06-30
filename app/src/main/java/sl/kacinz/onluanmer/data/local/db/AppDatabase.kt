package sl.kacinz.onluanmer.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import sl.kacinz.onluanmer.data.local.dao.GoalDao
import sl.kacinz.onluanmer.domain.model.Goal

@Database(entities = [Goal::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
}
