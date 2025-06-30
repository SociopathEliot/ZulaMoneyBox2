package sl.kacinz.onluanmer.domain.usecase

import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.repository.GoalRepository
import sl.kacinz.onluanmer.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(transaction: Transaction, updatedGoal: Goal) {
        transactionRepository.insertTransaction(transaction)
        goalRepository.updateGoal(updatedGoal)
    }
}
