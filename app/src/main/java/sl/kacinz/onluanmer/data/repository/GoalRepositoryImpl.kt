package sl.kacinz.onluanmer.data.repository

import sl.kacinz.onluanmer.data.local.GoalEntity



import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sl.kacinz.onluanmer.data.local.dao.GoalDao
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(
    private val dao: GoalDao
) : GoalRepository {

    override fun getGoals(): Flow<List<Goal>> =
        dao.getAll().map { list -> list.map(GoalEntity::toDomain) }

    override suspend fun insertGoal(goal: Goal) {
        dao.insert(goal.toEntity())
    }

    override suspend fun getGoalById(id: Long): Goal? =
        dao.findById(id)?.toDomain()
}

// extension mappers
private fun GoalEntity.toDomain() = Goal(
    id            = id,
    name          = name,
    targetAmount  = targetAmount,
    currentAmount = currentAmount,
    imageUri      = imageUri,
    targetDate    = targetDate
)

private fun Goal.toEntity() = GoalEntity(
    id            = id,
    name          = name,
    targetAmount  = targetAmount,
    currentAmount = currentAmount,
    imageUri      = imageUri,
    targetDate    = targetDate
)

