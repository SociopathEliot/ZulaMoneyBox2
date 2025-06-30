package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.usecase.GetGoalsUseCase
import javax.inject.Inject

@HiltViewModel
class GoalListViewModel @Inject constructor(
    getGoalsUseCase: GetGoalsUseCase
) : ViewModel() {
    val goals: StateFlow<List<Goal>> = getGoalsUseCase()
        .map { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}