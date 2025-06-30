package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import sl.kacinz.onluanmer.databinding.FragmentGoalDetailBinding
import java.text.NumberFormat

class GoalDetailFragment : Fragment() {

    private var _binding: FragmentGoalDetailBinding? = null
    private val binding get() = _binding!!

    // типобезопасный аргумент Goal
    private val args: GoalDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goal = args.goal

        // Форматтер чисел с разделителями групп
        val numberFormatter: NumberFormat =
            NumberFormat.getIntegerInstance().apply { isGroupingUsed = true }

        // Заполняем UI данными цели
        binding.tvGoalName.text = goal.name
        Glide.with(this)
            .load(goal.imageUri)
            .into(binding.ivGoalImage)

        val target = goal.targetAmount
        val current = goal.currentAmount
        binding.pbGoal.max = target
        binding.pbGoal.progress = current

        // Текст прогресса: "1 200$ / 5 000$"
        val currentStr = numberFormatter.format(current)
        val targetStr = numberFormatter.format(target)
        binding.tvProgressText.text = "$currentStr$ / $targetStr$"

        // Процент выполнения
        val percent = if (target > 0) (current * 100 / target) else 0
        binding.tvPercent.text = "$percent%"

        // Обработка возврата назад
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}