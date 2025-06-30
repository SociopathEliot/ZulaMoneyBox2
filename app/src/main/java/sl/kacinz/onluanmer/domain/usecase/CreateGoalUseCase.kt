package sl.kacinz.onluanmer.domain.usecase

import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import javax.inject.Inject

class CreateGoalUseCase @Inject constructor(private val repository: GoalRepository) {
    suspend operator fun invoke(goal: Goal) = repository.insertGoal(goal)
}