package sl.kacinz.onluanmer.domain.usecase

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import javax.inject.Inject

class GetTotalCurrentAmountUseCase @Inject constructor(
    private val repository: GoalRepository,
) {
    operator fun invoke(): Flow<Int?> = repository.getTotalCurrentAmount()
}
