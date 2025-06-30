package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sl.kacinz.onluanmer.databinding.FragmentWithdrawTransactionBinding
import sl.kacinz.onluanmer.domain.model.Transaction
import sl.kacinz.onluanmer.presentation.ui.fragments.viewmodels.AddTransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class WithdrawTransactionFragment : Fragment() {

    private var _binding: FragmentWithdrawTransactionBinding? = null
    private val binding get() = _binding!!

    private val args: WithdrawTransactionFragmentArgs by navArgs()
    private val viewModel: AddTransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWithdrawTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvDate.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        binding.btnSave.setOnClickListener { saveTransaction() }
    }

    private fun saveTransaction() {
        val amount = binding.etAmount.text.toString().toIntOrNull()
        if (amount == null) {
            Toast.makeText(requireContext(), "Enter amount", Toast.LENGTH_SHORT).show()
            return
        }
        val comment = binding.etComment.text.toString()
        val goal = args.goal
        val newAmount = goal.currentAmount - amount
        val updatedGoal = goal.copy(currentAmount = newAmount)
        val transaction = Transaction(
            goalId = goal.id,
            amount = -amount,
            comment = comment,
            date = binding.tvDate.text.toString()
        )
        viewModel.saveTransaction(transaction, updatedGoal)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
