package sl.kacinz.onluanmer.domain.usecase

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import javax.inject.Inject


class GetGoalsUseCase @Inject constructor(private val repository: GoalRepository) {
    operator fun invoke(): Flow<List<Goal>> = repository.getGoals()
}