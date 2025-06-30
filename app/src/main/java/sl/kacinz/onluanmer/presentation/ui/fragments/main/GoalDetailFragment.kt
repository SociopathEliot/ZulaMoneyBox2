package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import sl.kacinz.onluanmer.databinding.FragmentGoalDetailBinding
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.presentation.ui.adapters.TransactionAdapter
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.GoalDetailViewModel
import java.text.NumberFormat

class GoalDetailFragment : Fragment() {

    private var _binding: FragmentGoalDetailBinding? = null
    private val binding get() = _binding!!

    // типобезопасный аргумент Goal
    private val args: GoalDetailFragmentArgs by navArgs()
    private val viewModel: GoalDetailViewModel by viewModels()
    private val adapter = TransactionAdapter()
    private var currentGoal: Goal? = null
    private val numberFormatter: NumberFormat =
        NumberFormat.getIntegerInstance().apply { isGroupingUsed = true }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentGoal = args.goal
        updateGoalUI(currentGoal!!)

        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Goal>("updated_goal")
            ?.observe(viewLifecycleOwner, Observer { goal ->
                currentGoal = goal
                updateGoalUI(goal)
            })

        binding.rvTransactions.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            currentGoal?.let { goal ->
                viewModel.transactions(goal.id).collect { adapter.submitList(it) }
            }
        }

        binding.btnAddSaving.setOnClickListener {
            currentGoal?.let { goal ->
                val action = GoalDetailFragmentDirections
                    .actionGoalDetailFragmentToAddTransactionFragment(goal, true)
                findNavController().navigate(action)
            }
        }
        binding.btnWithdraw.setOnClickListener {
            currentGoal?.let { goal ->
                val action = GoalDetailFragmentDirections
                    .actionGoalDetailFragmentToWithdrawTransactionFragment(goal, false)
                findNavController().navigate(action)
            }
        }

        // Обработка возврата назад
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun updateGoalUI(goal: Goal) {
        binding.tvGoalName.text = goal.name
        Glide.with(this)
            .load(goal.imageUri)
            .into(binding.ivGoalImage)

        val target = goal.targetAmount
        val current = goal.currentAmount
        binding.pbGoal.max = target
        binding.pbGoal.progress = current

        val currentStr = numberFormatter.format(current)
        val targetStr = numberFormatter.format(target)
        binding.tvProgressText.text = "$currentStr$ / $targetStr$"

        val percent = if (target > 0) (current * 100 / target) else 0
        binding.tvPercent.text = "$percent%"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}