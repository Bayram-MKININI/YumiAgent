package net.noliaware.yumi_agent.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.Args.ACCOUNT_DATA
import net.noliaware.yumi_agent.commun.FragmentTags.PRIVACY_POLICY_FRAGMENT_TAG
import net.noliaware.yumi_agent.commun.util.ViewModelState
import net.noliaware.yumi_agent.commun.util.formatNumber
import net.noliaware.yumi_agent.commun.util.handleSharedEvent
import net.noliaware.yumi_agent.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_agent.commun.util.withArgs
import net.noliaware.yumi_agent.feature_auth.presentation.controllers.PrivacyPolicyFragment
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData
import net.noliaware.yumi_agent.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_agent.feature_profile.presentation.views.ProfileParentView
import net.noliaware.yumi_agent.feature_profile.presentation.views.ProfileView
import net.noliaware.yumi_agent.feature_profile.presentation.views.ProfileView.ProfileViewAdapter

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData?
        ) = UserProfileFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var profileDataParentView: ProfileParentView? = null
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_layout, container, false).apply {
            profileDataParentView = this as ProfileParentView
            profileDataParentView?.getProfileView?.callback = profileViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                profileDataParentView?.activateLoading(false)
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> profileDataParentView?.activateLoading(true)
                    is ViewModelState.DataState -> vmState.data?.let { userProfile ->
                        profileDataParentView?.activateLoading(false)
                        bindViewToData(userProfile)
                    }
                }
            }
        }
    }

    private fun bindViewToData(userProfile: UserProfile) {
        ProfileViewAdapter(
            login = userProfile.login.orEmpty(),
            surname = userProfile.lastName,
            name = userProfile.firstName,
            phone = userProfile.cellPhoneNumber.orEmpty(),
            email = userProfile.email.orEmpty(),
            service = userProfile.service.orEmpty(),
            usersValue = resources.getQuantityString(
                R.plurals.account_stat_format,
                userProfile.userCount,
                userProfile.userCount.formatNumber()
            ),
            retailersValue = resources.getQuantityString(
                R.plurals.account_stat_format,
                userProfile.retailerCount,
                userProfile.retailerCount.formatNumber()
            ),
            partnersValue = resources.getQuantityString(
                R.plurals.account_stat_format,
                userProfile.partnerCount,
                userProfile.partnerCount.formatNumber()
            ),
            contributorsValue = resources.getQuantityString(
                R.plurals.account_stat_format,
                userProfile.contributorCount,
                userProfile.contributorCount.formatNumber()
            )
        ).also {
            profileDataParentView?.getProfileView?.fillViewWithData(it)
        }
    }

    private val profileViewCallback: ProfileView.ProfileViewCallback by lazy {
        ProfileView.ProfileViewCallback {
            PrivacyPolicyFragment.newInstance(
                privacyPolicyUrl = viewModel.accountData?.privacyPolicyUrl.orEmpty(),
                isConfirmationRequired = false
            ).show(
                childFragmentManager.beginTransaction(),
                PRIVACY_POLICY_FRAGMENT_TAG
            )
        }
    }

    override fun onDestroyView() {
        profileDataParentView = null
        super.onDestroyView()
    }
}