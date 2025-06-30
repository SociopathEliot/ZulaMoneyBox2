package sl.kacinz.onluanmer.domain.usecase

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(goalId: Int): Flow<List<Transaction>> = repository.getTransactions(goalId)
}
