package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentGoalDetailBinding


class GoalDetailFragment : Fragment() {

    private lateinit var binding: FragmentGoalDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGoalDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

}