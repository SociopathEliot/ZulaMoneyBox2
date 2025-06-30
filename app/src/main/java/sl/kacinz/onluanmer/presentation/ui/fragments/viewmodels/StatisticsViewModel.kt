package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.usecase.GetAllTransactionsUseCase
import sl.kacinz.onluanmer.domain.usecase.GetGoalsUseCase
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    getGoalsUseCase: GetGoalsUseCase,
    getAllTransactionsUseCase: GetAllTransactionsUseCase
) : ViewModel() {
    val goals: StateFlow<List<Goal>> = getGoalsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val transactions: StateFlow<List<Transaction>> = getAllTransactionsUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
