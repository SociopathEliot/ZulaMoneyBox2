package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.domain.usecase.CreateGoalUseCase
import javax.inject.Inject

@HiltViewModel
class CreateGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase
) : ViewModel() {
    fun createGoal(goal: Goal) {
        viewModelScope.launch { createGoalUseCase(goal) }
    }
}