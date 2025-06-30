package sl.kacinz.onluanmer.domain.usecase

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.repository.TransactionRepository
import javax.inject.Inject

class GetAllTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getAllTransactions()
}
