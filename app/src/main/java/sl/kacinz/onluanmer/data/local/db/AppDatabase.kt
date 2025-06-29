package sl.kacinz.onluanmer.data.local.db



import androidx.room.Database
import androidx.room.RoomDatabase
import sl.kacinz.onluanmer.data.local.GoalEntity
import sl.kacinz.onluanmer.data.local.dao.GoalDao

@Database(
    entities = [GoalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
}
