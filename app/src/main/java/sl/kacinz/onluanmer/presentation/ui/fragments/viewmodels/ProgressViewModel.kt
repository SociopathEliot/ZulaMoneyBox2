package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.domain.usecase.GetTransactionsUseCase
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {
    fun transactions(goalId: Int): StateFlow<List<Transaction>> =
        getTransactionsUseCase(goalId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
