package sl.kacinz.onluanmer.data.repository

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.data.local.dao.GoalDao
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {
    override fun getGoals(): Flow<List<Goal>> = goalDao.getGoals()
    override suspend fun insertGoal(goal: Goal) = goalDao.insertGoal(goal)
    override suspend fun updateGoal(goal: Goal) = goalDao.insertGoal(goal)
}
