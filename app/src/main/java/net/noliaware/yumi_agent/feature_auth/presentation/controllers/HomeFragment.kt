package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.util.safeNavigate
import net.noliaware.yumi_agent.feature_auth.presentation.views.HomeMenuView.*
import net.noliaware.yumi_agent.feature_auth.presentation.views.HomeView
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData
import net.noliaware.yumi_agent.feature_message.presentation.controllers.MessagingFragmentArgs
import net.noliaware.yumi_agent.feature_profile.presentation.controllers.UserProfileFragmentArgs
import java.time.Duration

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var homeView: HomeView? = null
    private val args by navArgs<HomeFragmentArgs>()
    private val homeNavController by lazy {
        (childFragmentManager.findFragmentById(
            R.id.home_nav_host_fragment
        ) as NavHostFragment).findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_layout, container, false)?.apply {
            homeView = this as HomeView
            homeView?.homeMenuView?.callback = homeMenuViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBackButtonIntercept()
        homeView?.selectHomeButton()
        homeNavController.setGraph(
            R.navigation.home_nav_graph,
            AuthFragmentArgs(args.accountData).toBundle()
        )
        setUpBadges(args.accountData)
        showPrivacyPolicyDialogIfAny(args.accountData)
    }

    private fun setUpBadges(accountData: AccountData) {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(Duration.ofMillis(1000))
            homeView?.homeMenuView?.let { homeMenuView ->
                if (accountData.newMessageCount > 0) {
                    homeMenuView.setBadgeForMailButton(accountData.newMessageCount)
                }
                if (accountData.newAlertCount > 0) {
                    homeMenuView.setBadgeForNotificationButton(accountData.newAlertCount)
                }
            }
        }
    }

    private fun showPrivacyPolicyDialogIfAny(accountData: AccountData) {
        if (accountData.shouldConfirmPrivacyPolicy) {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(Duration.ofMillis(150))
                findNavController().safeNavigate(
                    HomeFragmentDirections.actionHomeFragmentToPrivacyPolicyFragment(
                        privacyPolicyUrl = accountData.privacyPolicyUrl,
                        isPrivacyPolicyConfirmationRequired = true
                    )
                )
            }
        }
    }

    private val homeMenuViewCallback: HomeMenuViewCallback by lazy {
        object : HomeMenuViewCallback {
            override fun onHomeButtonClicked() {
                homeView?.selectHomeButton()
                val navOption = NavOptions.Builder().setPopUpTo(
                    R.id.auth_fragment,
                    true
                ).build()
                homeNavController.navigate(
                    R.id.auth_fragment,
                    AuthFragmentArgs(args.accountData).toBundle(),
                    navOption
                )
            }

            override fun onProfileButtonClicked() {
                val navOption = NavOptions.Builder().setPopUpTo(
                    R.id.user_profile_fragment,
                    true
                ).build()
                homeNavController.navigate(
                    R.id.user_profile_fragment,
                    UserProfileFragmentArgs(args.accountData).toBundle(),
                    navOption
                )
            }

            override fun onMailButtonClicked() {
                homeView?.homeMenuView?.hideMailButtonBadge()
                val navOption = NavOptions.Builder().setPopUpTo(
                    R.id.messaging_fragment,
                    true
                ).build()
                homeNavController.navigate(
                    R.id.messaging_fragment,
                    MessagingFragmentArgs(args.accountData.domainName.orEmpty()).toBundle(),
                    navOption
                )
            }

            override fun onNotificationButtonClicked() {
                homeView?.homeMenuView?.hideNotificationButtonBadge()
                val navOption = NavOptions.Builder().setPopUpTo(
                    R.id.alerts_fragment,
                    true
                ).build()
                homeNavController.navigate(
                    R.id.alerts_fragment,
                    null,
                    navOption
                )
            }
        }
    }

    private fun setUpBackButtonIntercept() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when {
                        homeNavController.graph.startDestinationId != homeNavController.currentDestination?.id -> homeView?.performClickOnHomeButton()
                        else -> activity?.finish()
                    }
                }
            })
    }

    override fun onDestroyView() {
        homeView = null
        super.onDestroyView()
    }
}