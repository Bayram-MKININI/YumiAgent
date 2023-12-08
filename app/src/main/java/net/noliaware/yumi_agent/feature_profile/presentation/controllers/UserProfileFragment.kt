package net.noliaware.yumi_agent.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.util.ViewState
import net.noliaware.yumi_agent.commun.util.collectLifecycleAware
import net.noliaware.yumi_agent.commun.util.formatNumber
import net.noliaware.yumi_agent.commun.util.handleSharedEvent
import net.noliaware.yumi_agent.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_agent.commun.util.safeNavigate
import net.noliaware.yumi_agent.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_agent.feature_profile.presentation.views.ProfileParentView
import net.noliaware.yumi_agent.feature_profile.presentation.views.ProfileView
import net.noliaware.yumi_agent.feature_profile.presentation.views.ProfileView.ProfileViewAdapter

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileDataParentView: ProfileParentView? = null
    private val args by navArgs<UserProfileFragmentArgs>()
    private val viewModel by viewModels<UserProfileFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.profile_layout,
        container,
        false
    ).apply {
        profileDataParentView = this as ProfileParentView
        profileDataParentView?.getProfileView?.callback = profileViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileDataParentView?.activateLoading(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewModel.eventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            profileDataParentView?.activateLoading(false)
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.eventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.LoadingState -> Unit
                is ViewState.DataState -> viewState.data?.let { userProfile ->
                    profileDataParentView?.activateLoading(false)
                    bindViewToData(userProfile)
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
            findNavController().safeNavigate(
                UserProfileFragmentDirections.actionUserProfileFragmentToPrivacyPolicyFragment(
                    privacyPolicyUrl = args.accountData.privacyPolicyUrl,
                    isPrivacyPolicyConfirmationRequired = false
                )
            )
        }
    }

    override fun onDestroyView() {
        profileDataParentView = null
        super.onDestroyView()
    }
}