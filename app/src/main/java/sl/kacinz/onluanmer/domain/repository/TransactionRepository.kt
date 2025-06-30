package sl.kacinz.onluanmer.domain.repository

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.domain.model.Transaction

interface TransactionRepository {
    fun getTransactions(goalId: Int): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)
}
