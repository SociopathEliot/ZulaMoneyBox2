package sl.kacinz.onluanmer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Goal

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals")
    fun getGoals(): Flow<List<Goal>>

    @Query("SELECT SUM(currentAmount) FROM goals")
    fun getTotalCurrentAmount(): Flow<Int?>

    @Query("SELECT COUNT(*) FROM goals WHERE currentAmount < targetAmount")
    fun getActiveGoalsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: Goal)

    @androidx.room.Update
    suspend fun updateGoal(goal: Goal)
}