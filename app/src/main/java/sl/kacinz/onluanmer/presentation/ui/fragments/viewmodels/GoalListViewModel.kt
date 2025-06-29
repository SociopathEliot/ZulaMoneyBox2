package sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import sl.kacinz.onluanmer.domain.usecase.GetGoalsUseCase
import javax.inject.Inject

@HiltViewModel
class GoalListViewModel @Inject constructor(
    private val getGoalsUseCase: GetGoalsUseCase
) : ViewModel() {
    val goals = getGoalsUseCase().asLiveData()
}
