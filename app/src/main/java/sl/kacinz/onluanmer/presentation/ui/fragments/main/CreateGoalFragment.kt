package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sl.kacinz.onluanmer.databinding.FragmentCreateGoalBinding
import sl.kacinz.onluanmer.domain.model.Goal
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.CreateGoalViewModel
import java.util.Calendar

@AndroidEntryPoint
class CreateGoalFragment : Fragment() {

    private var _binding: FragmentCreateGoalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateGoalViewModel by viewModels()

    private var imageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
            val iv = ImageView(requireContext())
            iv.setImageURI(it)
            binding.imagePicker.removeAllViews()
            binding.imagePicker.addView(iv)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCreateGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etDate.setOnClickListener { openDatePicker() }
        binding.imagePicker.setOnClickListener { imagePickerLauncher.launch("image/*") }
        binding.btnGetStarted.setOnClickListener { saveGoal() }
    }

    private fun openDatePicker() {
        val c = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            val formatted = String.format("%02d.%02d.%04d", day, month + 1, year)
            binding.tvDate.setText(formatted)
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun saveGoal() {
        val name = binding.etGoalName.text.toString()
        val amount = binding.etTargetAmount.text.toString()
        val date = binding.tvDate.text.toString()
        val img = imageUri?.toString() ?: return
        if (name.isNotBlank() && amount.isNotBlank() && date.isNotBlank()) {
            viewModel.createGoal(
                Goal(name = name, targetAmount = amount, date = date, imageUri = img)
            )
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


