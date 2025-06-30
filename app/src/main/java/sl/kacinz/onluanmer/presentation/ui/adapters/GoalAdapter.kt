package sl.kacinz.onluanmer.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sl.kacinz.onluanmer.databinding.ItemGoalBinding
import sl.kacinz.onluanmer.domain.model.Goal
import java.text.NumberFormat

class GoalAdapter(
    private val onClick: (Goal) -> Unit
) : ListAdapter<Goal, GoalAdapter.GoalViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GoalViewHolder(
        private val binding: ItemGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val numberFormatter: NumberFormat =
            NumberFormat.getIntegerInstance().apply { isGroupingUsed = true }

        fun bind(goal: Goal) {
            // Название цели
            binding.tvGoalTitle.text = goal.name

            // Изображение цели
            Glide.with(binding.ivGoalImage)
                .load(goal.imageUri)
                .into(binding.ivGoalImage)

            // Прогресс
            val target = goal.targetAmount
            val current = goal.currentAmount
            binding.pbGoal.max = target
            binding.pbGoal.progress = current

            // Форматированный текст прогресса
            val currentStr = numberFormatter.format(current)
            val targetStr = numberFormatter.format(target)
            binding.tvProgressText.text = "$currentStr\$ / $targetStr\$"

            // Процент выполнения
            val percent = if (target > 0) (current * 100 / target) else 0
            binding.tvGoalPercent.text = "$percent%"

            // Обработчик клика
            binding.root.setOnClickListener { onClick(goal) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean =
            oldItem == newItem
    }
}
