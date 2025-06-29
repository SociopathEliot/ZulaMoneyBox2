package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.usecase.CreateGoalUseCase

@HiltViewModel
class CreateGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase
) : ViewModel() {
    fun create(goal: Goal) = viewModelScope.launch {
        createGoalUseCase(goal)
    }
}
