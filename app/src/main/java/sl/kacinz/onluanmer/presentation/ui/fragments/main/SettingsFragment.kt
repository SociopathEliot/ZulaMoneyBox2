package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentSettingsBinding
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.goalListFragment)
            }
        })

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.goalListFragment, false)
        }
        binding.btnResetAll.setOnClickListener {
            showConfirmResetDialog()
        }
        binding.llSelectCurrency.setOnClickListener {
            showSelectCurrencyDialog()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currency.collectLatest { cur ->
                cur?.let { binding.tvCurrentCurrency.text = it }
            }
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun showSelectCurrencyDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_currency, null, false)

        val currencies = arrayOf("CNY", "CAD", "AUD", "USD", "EUR", "GBP", "JPY")
        val np = dialogView.findViewById<NumberPicker>(R.id.np_currencies)

        // Настройка NumberPicker
        np.minValue = 0
        np.maxValue = currencies.size - 1
        np.displayedValues = currencies
        np.wrapSelectorWheel = false
        np.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        // Установка текущего значения
        val current = binding.tvCurrentCurrency.text.toString()
        val idx = currencies.indexOf(current).takeIf { it >= 0 } ?: 0
        np.value = idx

        // Скрываем черные разделители
        try {
            val fields = NumberPicker::class.java.declaredFields
            for (field in fields) {
                if (field.name == "mSelectionDivider") {
                    field.isAccessible = true
                    field.set(np, null)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Меняем цвет текста у невыбранных значений
        try {
            val selectorWheelPaintField = NumberPicker::class.java.getDeclaredField("mSelectorWheelPaint")
            selectorWheelPaintField.isAccessible = true
            val paint = selectorWheelPaintField.get(np) as Paint
            paint.color = Color.parseColor("#AAAAAA")
            paint.textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Меняем стиль текста у центрального элемента (черный жирный)
        styleSelectedNumber(np)

        // Следим за изменениями чтобы сохранять черный цвет выбранного значения
        np.setOnValueChangedListener { _, _, _ ->
            styleSelectedNumber(np)
        }
        np.setOnScrollListener { _, state ->
            if (state == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE ||
                state == NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
            ) {
                styleSelectedNumber(np)
            }
        }

        // Создаем диалог
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        // Крестик
        dialogView.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            dialog.dismiss()
        }

        // OK
        dialogView.findViewById<MaterialButton>(R.id.btn_ok).setOnClickListener {
            val selected = currencies[np.value]
            binding.tvCurrentCurrency.text = selected
            viewModel.setCurrency(selected)
            dialog.dismiss()
        }
    }



    private fun showConfirmResetDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_confirm_reset, null, false)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        view.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }
        view.findViewById<MaterialButton>(R.id.btn_delete).setOnClickListener {
            clearAllAppData()
            dialog.dismiss()
        }
    }

    private fun clearAllAppData() {
        viewModel.clearAll()
        Toast.makeText(requireContext(), "All data reset", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack(R.id.goalListFragment, false)
    }

    private fun styleSelectedNumber(np: NumberPicker) {
        for (i in 0 until np.childCount) {
            val child = np.getChildAt(i)
            if (child is EditText) {
                child.setTextColor(ColorStateList.valueOf(Color.BLACK))
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                child.setTypeface(null, Typeface.BOLD)
                child.isFocusable = false
                child.isClickable = false
            }
        }
    }
}