package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.ACCOUNT_DATA
import net.noliaware.yumi_agent.commun.BO_SIGN_IN_FRAGMENT_TAG
import net.noliaware.yumi_agent.commun.DAY_OF_MONTH_TEXT_DATE_FORMAT
import net.noliaware.yumi_agent.commun.HOURS_TIME_FORMAT
import net.noliaware.yumi_agent.commun.util.inflate
import net.noliaware.yumi_agent.commun.util.parseTimestampToDate
import net.noliaware.yumi_agent.commun.util.withArgs
import net.noliaware.yumi_agent.feature_auth.presentation.views.AuthView
import net.noliaware.yumi_agent.feature_auth.presentation.views.AuthView.AuthViewCallback
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData
import net.noliaware.yumi_agent.feature_login.domain.model.TFAMode

@AndroidEntryPoint
class AuthFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData?
        ) = AuthFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var authView: AuthView? = null
    private val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.auth_layout)?.apply {
            authView = this as AuthView
            authView?.callback = authViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.accountData?.let { accountData ->
            bindViewToData(accountData)
        }
    }

    private fun bindViewToData(accountData: AccountData) {
        authView?.setUserData(
            helloText = accountData.helloMessage,
            userName = accountData.userName,
            lastLoginTitle = accountData.lastConnectionTimestamp?.let {
                getString(R.string.last_login)
            } ?: getString(R.string.welcome_to_yumi),
            lastLoginValue = accountData.lastConnectionTimestamp?.let {
                getString(
                    R.string.last_login_value,
                    it.parseTimestampToDate(DAY_OF_MONTH_TEXT_DATE_FORMAT),
                    it.parseTimestampToDate(HOURS_TIME_FORMAT)
                )
            }
        )

        AuthView.AuthViewAdapter(
            twoFactorAuthModeText = map2FAModeText(accountData.twoFactorAuthMode),
            twoFactorAuthModeActivated = map2FAModeActivation(accountData.twoFactorAuthMode),
        ).also {
            authView?.fillViewWithData(it)
        }
    }

    private fun map2FAModeText(
        twoFactorAuthMode: TFAMode?
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> getString(R.string.bo_two_factor_auth_by_app)
        TFAMode.MAIL -> getString(R.string.bo_two_factor_auth_by_mail)
        else -> getString(R.string.bo_two_factor_auth_none)
    }

    private fun map2FAModeActivation(
        twoFactorAuthMode: TFAMode?
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> true
        else -> false
    }

    private val authViewCallback: AuthViewCallback by lazy {
        AuthViewCallback {
            BOSignInFragment.newInstance().show(
                childFragmentManager.beginTransaction(),
                BO_SIGN_IN_FRAGMENT_TAG
            )
        }
    }

    override fun onDestroyView() {
        authView = null
        super.onDestroyView()
    }
}