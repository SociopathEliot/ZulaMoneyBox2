package sl.kacinz.onluanmer.presentation.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sl.kacinz.onluanmer.databinding.ItemTransactionBinding
import sl.kacinz.onluanmer.domain.model.Transaction
import kotlin.math.abs

class TransactionAdapter : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            val sign = if (transaction.amount >= 0) "+" else "-"
            binding.tvAmount.text = "$sign${abs(transaction.amount)}$"
            if (transaction.amount < 0) {
                binding.tvAmount.setTextColor(Color.parseColor("#FF0004"))
            } else {
                binding.tvAmount.setTextColor(Color.parseColor("#55FF00"))
            }
            binding.tvDate.text = transaction.date
        }
    }
    private object DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean = oldItem == newItem
    }
}
