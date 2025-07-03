package sl.kacinz.onluanmer.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sl.kacinz.onluanmer.databinding.ItemMonthBinding
import sl.kacinz.onluanmer.utils.toKString

data class MonthAmount(val name: String, val amount: Int)

class MonthAdapter : ListAdapter<MonthAmount, MonthAdapter.MonthViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MonthViewHolder(private val binding: ItemMonthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MonthAmount) {
            binding.tvMonth.text = item.name
            binding.tvAmount.text = "${item.amount.toKString()}$"
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<MonthAmount>() {
        override fun areItemsTheSame(oldItem: MonthAmount, newItem: MonthAmount): Boolean = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: MonthAmount, newItem: MonthAmount): Boolean = oldItem == newItem
    }
}
