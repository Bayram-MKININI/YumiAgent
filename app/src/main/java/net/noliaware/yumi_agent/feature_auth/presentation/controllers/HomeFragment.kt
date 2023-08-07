package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.ACCOUNT_DATA
import net.noliaware.yumi_agent.commun.PRIVACY_POLICY_FRAGMENT_TAG
import net.noliaware.yumi_agent.commun.util.inflate
import net.noliaware.yumi_agent.commun.util.withArgs
import net.noliaware.yumi_agent.feature_alerts.presentation.controllers.AlertsFragment
import net.noliaware.yumi_agent.feature_auth.presentation.views.HomeMenuView
import net.noliaware.yumi_agent.feature_auth.presentation.views.HomeView
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData
import net.noliaware.yumi_agent.feature_message.presentation.controllers.MessagingFragment
import net.noliaware.yumi_agent.feature_profile.presentation.controllers.UserProfileFragment

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData
        ) = HomeFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var homeView: HomeView? = null
    private val viewModel by viewModels<AuthFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.home_layout)?.apply {
            homeView = this as HomeView
            homeView?.homeMenuView?.callback = homeMenuViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayAuthFragment()
        viewModel.accountData?.let { accountData ->
            homeView?.homeMenuView?.let { homeMenuView ->
                if (accountData.newMessageCount > 0) {
                    homeMenuView.setBadgeForMailButton(accountData.newMessageCount)
                }
                if (accountData.newAlertCount > 0) {
                    homeMenuView.setBadgeForNotificationButton(accountData.newAlertCount)
                }
            }

            if (accountData.shouldConfirmPrivacyPolicy) {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(150)
                    PrivacyPolicyFragment.newInstance(
                        privacyPolicyUrl = accountData.privacyPolicyUrl,
                        isConfirmationRequired = true
                    ).show(
                        childFragmentManager.beginTransaction(),
                        PRIVACY_POLICY_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    private val homeMenuViewCallback: HomeMenuView.HomeMenuViewCallback by lazy {
        object : HomeMenuView.HomeMenuViewCallback {
            override fun onCategoryButtonClicked() {
                displayAuthFragment()
            }

            override fun onProfileButtonClicked() {
                childFragmentManager.beginTransaction().run {
                    replace(
                        R.id.main_fragment_container,
                        UserProfileFragment.newInstance(viewModel.accountData)
                    )
                    commit()
                }
            }

            override fun onMailButtonClicked() {
                homeView?.homeMenuView?.hideMailButtonBadge()
                childFragmentManager.beginTransaction().run {
                    replace(
                        R.id.main_fragment_container,
                        MessagingFragment.newInstance(viewModel.accountData?.domainName)
                    )
                    commit()
                }
            }

            override fun onNotificationButtonClicked() {
                homeView?.homeMenuView?.hideNotificationButtonBadge()
                childFragmentManager.beginTransaction().run {
                    replace(R.id.main_fragment_container, AlertsFragment())
                    commit()
                }
            }
        }
    }

    private fun displayAuthFragment() {
        childFragmentManager.beginTransaction().run {
            replace(
                R.id.main_fragment_container,
                AuthFragment.newInstance(viewModel.accountData)
            )
            commit()
            homeView?.selectHomeButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeView = null
    }
}