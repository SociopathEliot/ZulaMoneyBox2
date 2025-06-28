package sl.kacinz.onluanmer.presentation.ui.fragments.onboarding

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import sl.kacinz.onluanmer.databinding.FragmentWelcomeBinding
import sl.kacinz.onluanmer.presentation.ui.fragments.main.HomeFragment
import sl.kacinz.onluanmer.utils.Constants.WELCOME_KEY
import sl.kacinz.onluanmer.utils.Constants.getSharedPreferences
import sl.kacinz.onluanmer.utils.Constants.launchNewFragmentWithoutBackstack

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        //todo welcome fragment logic
        binding.btnGetStarted.setOnClickListener {
            context?.getSharedPreferences()?.edit { putBoolean(WELCOME_KEY, true).apply() }
            parentFragmentManager.launchNewFragmentWithoutBackstack(HomeFragment())
        }
    }
}