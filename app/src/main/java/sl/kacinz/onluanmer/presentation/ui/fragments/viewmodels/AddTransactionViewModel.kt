package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.usecase.AddTransactionUseCase
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {
    suspend fun saveTransaction(transaction: Transaction, goal: Goal) {
        addTransactionUseCase(transaction, goal)
    }
}
