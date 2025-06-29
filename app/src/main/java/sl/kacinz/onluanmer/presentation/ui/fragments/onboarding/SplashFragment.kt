package sl.kacinz.onluanmer.presentation.ui.fragments.onboarding

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import sl.kacinz.onluanmer.databinding.FragmentSplashBinding
import sl.kacinz.onluanmer.presentation.ui.fragments.main.HomeFragment
import sl.kacinz.onluanmer.presentation.ui.fragments.legal.PrivacyPolicyFragment
import sl.kacinz.onluanmer.utils.Constants.DEFAULT_DOMAIN_LINK
import sl.kacinz.onluanmer.utils.Constants.MAIN_OFFER_LINK_KEY
import sl.kacinz.onluanmer.utils.Constants.USER_STATUS_KEY
import sl.kacinz.onluanmer.utils.Constants.WELCOME_KEY
import sl.kacinz.onluanmer.utils.Constants.getSharedPreferences
import sl.kacinz.onluanmer.utils.Constants.launchNewFragmentWithoutBackstack

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        handleAppInitialization()

        //todo splash animation logic
        val shader = LinearGradient(
            0f, 0f, binding.loadingText.paint.measureText(binding.loadingText.text.toString()), binding.loadingText.textSize,
            intArrayOf(
                Color.parseColor("#FEDD32"), // start
                Color.parseColor("#FFEB86"), // middle
                Color.parseColor("#FEDD32")  // end
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        binding.loadingText.paint.shader = shader

        Handler(Looper.getMainLooper()).postDelayed({
        }, 30_000L)
    }

    private fun navigateToProjectFragment() {
        val launchedBefore = context?.getSharedPreferences()?.getBoolean(WELCOME_KEY, false) == true
        if (launchedBefore) {
            parentFragmentManager.launchNewFragmentWithoutBackstack(HomeFragment())
        } else {
            parentFragmentManager.launchNewFragmentWithoutBackstack(WelcomeFragment())
        }
    }

    private fun handleAppInitialization() {
        val offerLink = context?.getSharedPreferences()?.getString(MAIN_OFFER_LINK_KEY, "") ?: ""
        if (!isUser()) {
            navigateToProjectFragment()
        } else if (offerLink.isNotEmpty()) {
            navigateBasedOnOfferLink(offerLink)
        } else {
            getLinks()
        }
    }

    private fun getLinks() {
        val queue = Volley.newRequestQueue(context)
        val url = DEFAULT_DOMAIN_LINK

        val stringRequest = object : StringRequest(Method.GET, url, Response.Listener { offerLink ->

            if (offerLink.isNullOrEmpty()) {
                saveUserFalse()
                navigateBasedOnOfferLink(offerLink)
            } else {
                saveLink(offerLink)
                navigateBasedOnOfferLink(offerLink)
            }
        }, Response.ErrorListener {
            navigateBasedOnOfferLink("")

        }) {}

        queue.add(stringRequest)
    }

    private fun navigateBasedOnOfferLink(offerLink: String) {
        if (offerLink.isNotEmpty()) {
            parentFragmentManager.launchNewFragmentWithoutBackstack(PrivacyPolicyFragment(offerLink))
        } else {
            navigateToProjectFragment()
        }
    }

    private fun saveLink(offerLink: String) {
        context?.getSharedPreferences()?.edit { putString(MAIN_OFFER_LINK_KEY, offerLink)?.apply() }
    }

    private fun saveUserFalse() {
        context?.getSharedPreferences()?.edit { putBoolean(USER_STATUS_KEY, false)?.apply() }
    }

    private fun isUser(): Boolean {
        return context?.getSharedPreferences()?.getBoolean(USER_STATUS_KEY, true) ?: true
    }
}