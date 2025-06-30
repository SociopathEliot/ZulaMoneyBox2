package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sl.kacinz.onluanmer.domain.usecase.ClearDataUseCase
import sl.kacinz.onluanmer.domain.usecase.GetCurrencyUseCase
import sl.kacinz.onluanmer.domain.usecase.SetCurrencyUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getCurrencyUseCase: GetCurrencyUseCase,
    private val setCurrencyUseCase: SetCurrencyUseCase,
    private val clearDataUseCase: ClearDataUseCase
) : ViewModel() {

    val currency: StateFlow<String?> = getCurrencyUseCase()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun setCurrency(cur: String) {
        viewModelScope.launch { setCurrencyUseCase(cur) }
    }

    fun clearAll() {
        viewModelScope.launch { clearDataUseCase() }
    }
}
