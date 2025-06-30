package sl.kacinz.onluanmer.presentation.ui.fragments.main

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import sl.kacinz.onluanmer.R
import sl.kacinz.onluanmer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //todo home fragment logic

        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        binding.customBottomNav.btnStatistics.setOnClickListener {
            navController.navigate(R.id.statisticsFragment)
        }

        binding.customBottomNav.btnSettings.setOnClickListener {
            navController.navigate(R.id.settingsFragment)
        }
    }
}