package sl.kacinz.onluanmer.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sl.kacinz.onluanmer.databinding.ItemGoalBinding
import sl.kacinz.onluanmer.domain.model.Goal

class GoalAdapter(
    private val onClick: (Goal) -> Unit
) : ListAdapter<Goal, GoalAdapter.GoalVH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Goal>() {
            override fun areItemsTheSame(old: Goal, new: Goal) = old.id == new.id
            override fun areContentsTheSame(old: Goal, new: Goal) = old == new
        }
    }

    inner class GoalVH(private val b: ItemGoalBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(goal: Goal) {
            b.tvGoalTitle.text = goal.name
            b.pbGoal.max = goal.targetAmount.toInt()
            b.pbGoal.progress = goal.currentAmount.toInt()
            b.tvProgressText.text = "%,d$ / %,d$".format(
                goal.currentAmount.toLong(),
                goal.targetAmount.toLong()
            )
            val percent = if (goal.targetAmount > 0)
                (goal.currentAmount / goal.targetAmount * 100).toInt()
            else 0
            b.tvGoalPercent.text = "$percent%"
            // TODO: load goal.imageUri into b.ivGoalImage with Glide/Picasso if you like
            b.cardGoal.setOnClickListener { onClick(goal) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GoalVH(ItemGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: GoalVH, pos: Int) =
        holder.bind(getItem(pos))
}
