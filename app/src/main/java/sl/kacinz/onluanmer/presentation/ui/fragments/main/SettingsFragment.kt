package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

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
            showSelectCurrencyDialog()
        }
        binding.llSelectCurrency.setOnClickListener {
            showSelectCurrencyDialog()
        }
    }

    private fun showSelectCurrencyDialog() {
        // 1) Inflate
        val dialogView = layoutInflater.inflate(R.layout.dialog_select_currency, null, false)

        // 2) Подготовить NumberPicker
        val currencies = arrayOf("CAD", "AUD", "USD", "EUR", "GBP", "JPY")
        val np = dialogView.findViewById<NumberPicker>(R.id.np_currencies)
        np.minValue = 0
        np.maxValue = currencies.size - 1
        np.displayedValues = currencies
        // выставим текущее значение
        val current = binding.tvCurrentCurrency.text.toString()
        val idx = currencies.indexOf(current).takeIf { it >= 0 } ?: 0
        np.value = idx

        // 3) Создать AlertDialog
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        // 4) Крестик
        dialogView.findViewById<ImageView>(R.id.iv_close).setOnClickListener {
            dialog.dismiss()
        }

        // 5) Кнопка OK
        dialogView.findViewById<MaterialButton>(R.id.btn_ok).setOnClickListener {
            val sel = currencies[np.value]
            binding.tvCurrentCurrency.text = sel
            dialog.dismiss()
            // TODO: тут ваш код сохранения выбора, если нужно
        }
    }

    private fun clearAllAppData() {
        // Здесь очищаем весь Room — пример:
        // val db = AppDatabase.getInstance(requireContext())
        // db.clearAllTables()

        Toast.makeText(requireContext(), "All data reset", Toast.LENGTH_SHORT).show()
        // При желании — сразу вернуться:
        findNavController().popBackStack(R.id.goalListFragment, false)
    }
}