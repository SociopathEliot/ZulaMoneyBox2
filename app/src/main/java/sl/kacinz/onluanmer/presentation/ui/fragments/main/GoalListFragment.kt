package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentCreateGoalBinding
import sl.kacinz.onluanmer.databinding.FragmentGoalListBinding
import sl.kacinz.onluanmer.presentation.ui.adapters.GoalAdapter
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.GoalListViewModel


@AndroidEntryPoint
class GoalListFragment : Fragment(R.layout.fragment_goal_list) {

    private val vm: GoalListViewModel by viewModels()
    private var _b: FragmentGoalListBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentGoalListBinding.bind(view)

        val adapter = GoalAdapter { goal ->
            // Step 1 nav → GoalDetailFragment
            findNavController().navigate(
                R.id.action_goalListFragment_to_goalDetailFragment,
                Bundle().apply { putLong("goalId", goal.id) }
            )
        }

        b.rvGoals.adapter = adapter

        // observe list of goals
        vm.goals.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        // Step 2 nav → CreateGoalFragment
        b.btnAddGoal.setOnClickListener {
            findNavController().navigate(R.id.action_goalListFragment_to_createGoalFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
