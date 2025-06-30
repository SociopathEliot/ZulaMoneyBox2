package sl.kacinz.onluanmer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sl.kacinz.onluanmer.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT defaultCurrency FROM settings WHERE id = 1 LIMIT 1")
    fun getCurrency(): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: AppSettings)

    @Query("DELETE FROM settings")
    suspend fun clear()
}
