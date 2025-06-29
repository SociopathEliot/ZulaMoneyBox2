package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentCreateGoalBinding
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.CreateGoalViewModel
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import sl.kacinz.onluanmer.domain.model.Goal
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateGoalFragment : Fragment(R.layout.fragment_create_goal) {

    private val vm: CreateGoalViewModel by viewModels()
    private var _b: FragmentCreateGoalBinding? = null
    private val b get() = _b!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _b = FragmentCreateGoalBinding.bind(view)

        b.btnGetStarted.setOnClickListener {
            val name   = b.etGoalName.text.toString().trim()
            val amount = b.etTargetAmount.text.toString().toDoubleOrNull()
            val dateStr= b.etDate.text.toString().trim()
            if (name.isEmpty() || amount == null || dateStr.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // parse dd.MM.yyyy → epoch millis
            val fmt = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val dateMillis = runCatching { fmt.parse(dateStr)!!.time }
                .getOrElse {
                    Toast.makeText(requireContext(), "Bad date format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            val goal = Goal(
                name          = name,
                targetAmount  = amount,
                targetDate    = dateMillis
            )

            // save to Room
            vm.create(goal)

            // back to list
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
