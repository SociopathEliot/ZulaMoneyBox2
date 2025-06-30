package sl.kacinz.onluanmer.data.repository

import kotlinx.coroutines.flow.Flow
import sl.kacinz.onluanmer.data.local.dao.TransactionDao
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {
    override fun getTransactions(goalId: Int): Flow<List<Transaction>> = dao.getTransactions(goalId)
    override suspend fun insertTransaction(transaction: Transaction) = dao.insertTransaction(transaction)
}
