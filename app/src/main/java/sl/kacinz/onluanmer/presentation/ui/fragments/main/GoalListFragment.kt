package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentCreateGoalBinding
import sl.kacinz.onluanmer.databinding.FragmentGoalListBinding

class GoalListFragment : Fragment() {

    private lateinit var binding: FragmentGoalListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalListBinding.inflate(inflater,container,false)
        return binding.root
    }

}