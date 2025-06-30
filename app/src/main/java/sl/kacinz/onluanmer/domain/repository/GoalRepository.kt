package sl.kacinz.onluanmer.domain.repository

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Goal

interface GoalRepository {
    fun getGoals(): Flow<List<Goal>>
    fun getTotalCurrentAmount(): Flow<Int?>
    fun getActiveGoalsCount(): Flow<Int>
    suspend fun insertGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
}