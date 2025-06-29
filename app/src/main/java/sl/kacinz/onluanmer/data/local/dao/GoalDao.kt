package sl.kacinz.onluanmer.data.local.dao



import androidx.room.*
import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.data.local.GoalEntity

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals")
    fun getAll(): Flow<List<GoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): GoalEntity?
}
