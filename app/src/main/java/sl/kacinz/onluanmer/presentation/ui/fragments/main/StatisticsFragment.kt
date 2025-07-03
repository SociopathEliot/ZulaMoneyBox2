package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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
import java.util.Locale

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.goalListFragment)
            }
        })

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
                    viewModel.totalAmount.collect { amount ->
                        val total = amount ?: 0
                        binding.tvTotalFunds.text = "${total}$"
                    }
                }
                launch {
                    viewModel.activeGoals.collect { count ->
                        binding.tvActiveGoals.text = count.toString()
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
        val parseFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val labelFmt = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)

        // Parse all dates and determine month range
        val parsedDates = transactions.map { LocalDate.parse(it.date, parseFmt) }
        if (parsedDates.isEmpty()) return

        val startMonth = YearMonth.from(parsedDates.minOrNull()!!)
        val lastMonth = YearMonth.from(parsedDates.maxOrNull()!!)
        val endForLabel = lastMonth.plusMonths(1)

        // Complete list of months in range (+1 for right gap)
        val months = generateSequence(startMonth) { it.plusMonths(1) }
            .takeWhile { !it.isAfter(endForLabel) }
            .toList()

        // Sum of transactions per month
        val sumsByMonth = transactions.groupBy { YearMonth.from(LocalDate.parse(it.date, parseFmt)) }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }

        val step = 100f
        val entries = months.dropLast(1).mapIndexed { idx, ym ->
            val raw = sumsByMonth[ym] ?: 0f
            val snapped = (raw / step).toInt() * step
            Entry(idx + 1f, snapped)
        }

        val labels = listOf("") + months.map { it.atDay(1).format(labelFmt) }

        val dataSet = LineDataSet(entries, "").apply {
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(false)
            color = Color.parseColor("#FFC107")
            lineWidth = 2f
            setDrawCircles(true)
            circleRadius = 6f
            circleHoleRadius = 3f
            setCircleColor(Color.parseColor("#FFC107"))
            circleHoleColor = Color.parseColor("#0D1B3D")
            setDrawCircleHole(true)
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_chart_fill)
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            setBackgroundColor(Color.parseColor("#0D1B3D"))
            setDrawGridBackground(false)
            setTouchEnabled(false)
            axisRight.isEnabled = false
            legend.isEnabled = false

            val dp = resources.displayMetrics.density
            setExtraOffsets(0f * dp, 0f, 0f * dp, 0f * dp)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(labels)
                granularity = 1f
                isGranularityEnabled = true
                setLabelCount(labels.size, true)
                setAvoidFirstLastClipping(true)
                setAxisMinimum(0f)
                setAxisMaximum((labels.size - 1).toFloat())
                textSize = 12f
                textColor = Color.WHITE
                setDrawAxisLine(false)
                setDrawGridLines(true)
                gridColor = Color.parseColor("#4D6F7D9C")
                gridLineWidth = 1f
            }

            axisLeft.apply {
                val maxY = dataSet.yMax
                val top = ((maxY / step).toInt() + 1) * step
                setAxisMinimum(0f)
                setAxisMaximum(top)
                setLabelCount((top / step).toInt() + 1, true)
                textSize = 12f
                textColor = Color.WHITE
                setDrawAxisLine(false)
                setDrawGridLines(true)
                gridColor = Color.parseColor("#4D6F7D9C")
                gridLineWidth = 1f
            }

            description.isEnabled = false
            animateX(500)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
