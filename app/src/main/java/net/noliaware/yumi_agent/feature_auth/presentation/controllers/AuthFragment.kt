package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.DateTime.DAY_OF_MONTH_TEXT_DATE_FORMAT
import net.noliaware.yumi_agent.commun.DateTime.HOURS_TIME_FORMAT
import net.noliaware.yumi_agent.commun.util.parseTimestampToDate
import net.noliaware.yumi_agent.commun.util.safeNavigate
import net.noliaware.yumi_agent.feature_auth.presentation.views.AuthView
import net.noliaware.yumi_agent.feature_auth.presentation.views.AuthView.AuthViewAdapter
import net.noliaware.yumi_agent.feature_auth.presentation.views.AuthView.AuthViewCallback
import net.noliaware.yumi_agent.feature_login.domain.model.AccountData
import net.noliaware.yumi_agent.feature_login.domain.model.TFAMode

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var authView: AuthView? = null
    private val args by navArgs<AuthFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.auth_layout,
        container,
        false
    ).apply {
        authView = this as AuthView
        authView?.callback = authViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewToData(args.accountData)
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

        AuthViewAdapter(
            twoFactorAuthModeText = map2FAModeText(accountData.twoFactorAuthMode),
            twoFactorAuthModeActivated = map2FAModeActivation(accountData.twoFactorAuthMode),
        ).also {
            authView?.fillViewWithData(it)
        }
    }

    private fun map2FAModeText(
        twoFactorAuthMode: TFAMode
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> getString(R.string.bo_two_factor_auth_by_app)
        TFAMode.MAIL -> getString(R.string.bo_two_factor_auth_by_mail)
        else -> getString(R.string.bo_two_factor_auth_none)
    }

    private fun map2FAModeActivation(
        twoFactorAuthMode: TFAMode
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> true
        else -> false
    }

    private val authViewCallback: AuthViewCallback by lazy {
        AuthViewCallback {
            findNavController().safeNavigate(
                AuthFragmentDirections.actionAuthFragmentToBOSignInFragment()
            )
        }
    }

    override fun onDestroyView() {
        authView?.callback = null
        authView = null
        super.onDestroyView()
    }
}