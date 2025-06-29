package sl.kacinz.onluanmer.domain.repository

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Goal

interface GoalRepository {
    fun getGoals(): Flow<List<Goal>>
    suspend fun insertGoal(goal: Goal)
    suspend fun getGoalById(id: Long): Goal?
}
