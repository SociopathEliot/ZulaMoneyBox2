package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sl.kacinz.onluanmer.databinding.FragmentProgressBinding
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.ProgressViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val args: ProgressFragmentArgs by navArgs()
    private val viewModel: ProgressViewModel by viewModels()

    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    private val ranges = listOf(
        TimeRange.LAST_WEEK,
        TimeRange.LAST_MONTH,
        TimeRange.LAST_YEAR,
        TimeRange.ALL_TIME
    )
    private var rangeIndex = 0

    private var allTransactions: List<Transaction> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGoal.text = args.goal.name
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnPrev.setOnClickListener { changeRange(-1) }
        binding.btnNext.setOnClickListener { changeRange(1) }
        binding.tvRange.text = ranges[rangeIndex].label

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.transactions(args.goal.id).collect { list ->
                    allTransactions = list
                    updateForRange()
                }
            }
        }
    }

    private fun changeRange(delta: Int) {
        rangeIndex = (rangeIndex + delta + ranges.size) % ranges.size
        binding.tvRange.text = ranges[rangeIndex].label
        updateForRange()
    }

    private fun updateForRange() {
        val filtered = filterTransactions(allTransactions, ranges[rangeIndex])
        updateInfo(filtered)
        updateChart(filtered)
    }

    private fun filterTransactions(
        transactions: List<Transaction>,
        range: TimeRange
    ): List<Transaction> {
        if (range.days == null) return transactions
        val cutoff = LocalDate.now().minusDays(range.days)
        return transactions.filter {
            val date = LocalDate.parse(it.date, formatter)
            !date.isBefore(cutoff)
        }
    }

    private fun updateInfo(transactions: List<Transaction>) {
        val total = transactions.sumOf { it.amount }
        val deposits = transactions.filter { it.amount > 0 }
        val average = if (deposits.isNotEmpty()) deposits.map { it.amount }.average() else 0.0
        binding.tvTotal.text = "${total}\$"
        binding.tvAverage.text = String.format("%.1f$", average)
        binding.tvCount.text = transactions.size.toString()
    }

    private fun updateChart(transactions: List<Transaction>) {
        val sorted = transactions.sortedBy { LocalDate.parse(it.date, formatter) }
        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        var sum = 0f
        sorted.forEachIndexed { index, tx ->
            sum += tx.amount
            entries.add(Entry(index.toFloat(), sum))
            labels.add(tx.date)
        }

        val dataSet = LineDataSet(entries, "").apply {
            color = Color.YELLOW
            setCircleColor(Color.YELLOW)
            valueTextColor = Color.WHITE
            lineWidth = 2f
            circleRadius = 4f
        }
        binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.lineChart.axisRight.isEnabled = false
        binding.lineChart.description = Description().apply { text = "" }
        binding.lineChart.data = LineData(dataSet)
        binding.lineChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class TimeRange(val label: String, val days: Long?) {
        LAST_WEEK("Last week", 7),
        LAST_MONTH("Last month", 30),
        LAST_YEAR("Last year", 365),
        ALL_TIME("All time", null)
    }
}
