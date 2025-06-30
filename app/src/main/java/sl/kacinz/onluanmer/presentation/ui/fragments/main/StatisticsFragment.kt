package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentStatisticsBinding
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.presentation.ui.adapters.MonthAdapter
import sl.kacinz.onluanmer.presentation.ui.adapters.MonthAmount
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.StatisticsViewModel
import sl.kacinz.onluanmer.utils.TimeRange
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StatisticsViewModel by viewModels()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val ranges = listOf(
        TimeRange.LAST_WEEK,
        TimeRange.LAST_MONTH,
        TimeRange.LAST_YEAR,
        TimeRange.ALL_TIME
    )
    private var rangeIndex = 1

    private var allTransactions: List<Transaction> = emptyList()

    private val monthAdapter = MonthAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTopMonths.adapter = monthAdapter
        binding.btnPrev.setOnClickListener { changeRange(-1) }
        binding.btnNext.setOnClickListener { changeRange(1) }
        binding.tvRange.text = ranges[rangeIndex].label
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.transactions.collect { list ->
                        allTransactions = list
                        updateForRange()
                    }
                }
                launch {
                    viewModel.goals.collect { goals ->
                        val total = goals.sumOf { it.currentAmount }
                        val active = goals.count { it.currentAmount < it.targetAmount }
                        binding.tvTotalFunds.text = "$total$"
                        binding.tvActiveGoals.text = active.toString()
                    }
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
        updateChart(filtered)
        updateMetrics(filtered)
    }

    private fun filterTransactions(
        transactions: List<Transaction>,
        range: TimeRange,
    ): List<Transaction> {
        if (range.days == null) return transactions
        val cutoff = LocalDate.now().minusDays(range.days)
        return transactions.filter {
            val date = LocalDate.parse(it.date, formatter)
            !date.isBefore(cutoff)
        }
    }

    private fun updateMetrics(transactions: List<Transaction>) {
        val deposits = transactions.filter { it.amount > 0 }
        val avgTxn = if (deposits.isNotEmpty()) deposits.map { it.amount }.average() else 0.0

        // Monthly totals
        val monthly = deposits.groupBy {
            val date = LocalDate.parse(it.date, formatter)
            YearMonth.from(date)
        }.map { (ym, list) ->
            val sum = list.sumOf { it.amount }
            val name = ym.month.name.lowercase().replaceFirstChar { c -> c.titlecase() } + " " + ym.year
            MonthAmount(name, sum)
        }.sortedByDescending { it.amount }

        monthAdapter.submitList(monthly.take(3))

        val avgMonth = if (monthly.isNotEmpty()) monthly.map { it.amount }.average() else 0.0

        binding.tvAvgPerTxn.text = String.format("%.1f$", avgTxn)
        binding.tvAvgPerMonth.text = String.format("%.1f$", avgMonth)
    }

    private fun updateChart(transactions: List<Transaction>) {
        val sorted = transactions.sortedBy { LocalDate.parse(it.date, formatter) }
        val entries = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        var sum = 0f
        sorted.forEachIndexed { i, tx ->
            sum += tx.amount
            entries.add(Entry(i.toFloat(), sum))
            labels.add(tx.date.substring(0,5))
        }

        val dataSet = LineDataSet(entries, "").apply {
            setDrawValues(false)
            mode = LineDataSet.Mode.LINEAR
            color = Color.parseColor("#FEDD32")
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 6f
            circleHoleRadius = 3f
            setCircleColor(Color.parseColor("#FEDD32"))
            circleHoleColor = Color.WHITE
            setDrawFilled(true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_chart_fill)
            } else {
                fillColor = Color.parseColor("#FEDD32")
                fillAlpha = 80
            }
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            setBackgroundColor(Color.parseColor("#001443"))
            setDrawGridBackground(false)
            setTouchEnabled(false)
            axisRight.isEnabled = false
            legend.isEnabled = false
            description = Description().apply {
                text = "Amount of savings"
                textColor = Color.WHITE
                textSize = 14f
                setPosition(10f,15f)
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(labels)
                granularity = 1f
                textColor = Color.WHITE
                textSize = 12f
                setDrawGridLines(true)
                gridColor = Color.parseColor("#445270")
                axisLineColor = Color.parseColor("#445270")
            }
            axisLeft.apply {
                textColor = Color.WHITE
                textSize = 12f
                setDrawGridLines(true)
                gridColor = Color.parseColor("#445270")
                axisLineColor = Color.parseColor("#445270")
            }
            animateX(500)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
