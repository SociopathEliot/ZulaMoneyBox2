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
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentProgressBinding
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.ProgressViewModel
import sl.kacinz.onluanmer.utils.TimeRange
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
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
        // 1. prepare entries & labels
        val sorted = transactions.sortedBy { LocalDate.parse(it.date, formatter) }
        val entries = mutableListOf<Entry>()
        val labels  = mutableListOf<String>()
        var sum = 0f
        sorted.forEachIndexed { i, tx ->
            sum += tx.amount
            entries.add(Entry(i.toFloat(), sum))
            labels.add(tx.date.substring(0, 5)) // "dd.MM"
        }

        // 2. configure the dataset
        val dataSet = LineDataSet(entries, "").apply {
            setDrawValues(false)
            mode = LineDataSet.Mode.LINEAR      // or CUBIC_BEZIER for smooth curve
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

        // 3. apply to chart
        binding.lineChart.apply {
            data = LineData(dataSet)
            setBackgroundColor(Color.parseColor("#001443"))
            setDrawGridBackground(false)
            setTouchEnabled(false)
            axisRight.isEnabled = false
            legend.isEnabled    = false

            // a) chart title
            description = Description().apply {
                text      = "Amount of savings"
                textColor = Color.WHITE
                textSize  = 14f
                setPosition(10f,  15f)
            }

            // b) X axis
            xAxis.apply {
                position        = XAxis.XAxisPosition.BOTTOM
                valueFormatter  = IndexAxisValueFormatter(labels)
                granularity     = 1f
                textColor       = Color.WHITE
                textSize        = 12f
                setDrawGridLines(true)
                gridColor       = Color.parseColor("#445270")
                axisLineColor   = Color.parseColor("#445270")
            }

            // c) Y axis
            axisLeft.apply {
                textColor       = Color.WHITE
                textSize        = 12f
                setDrawGridLines(true)
                gridColor       = Color.parseColor("#445270")
                axisLineColor   = Color.parseColor("#445270")
            }

            // optional: a quick animation
            animateX(500)
            invalidate()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
