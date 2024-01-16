package net.noliaware.yumi_agent.feature_auth.presentation.controllers

import android.os.Bundle
import android.text.SpannableString
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
import net.noliaware.yumi_agent.commun.util.DecoratedText
import net.noliaware.yumi_agent.commun.util.decorateWords
import net.noliaware.yumi_agent.commun.util.getFontFromResources
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
            helloText = getString(
                R.string.hello_user_format,
                accountData.helloMessage,
                accountData.userName
            ).decorateWords(
                wordsToDecorate = listOf(
                    decorateTextWithFont(accountData.userName),
                )
            ),
            lastLogin = accountData.lastConnectionTimestamp?.let {
                val date = it.parseTimestampToDate(DAY_OF_MONTH_TEXT_DATE_FORMAT)
                val time = it.parseTimestampToDate(HOURS_TIME_FORMAT)
                getString(
                    R.string.last_login,
                    date,
                    time
                ).decorateWords(
                    wordsToDecorate = listOf(
                        decorateTextWithFont(date),
                        decorateTextWithFont(time)
                    )
                )
            } ?: SpannableString(getString(R.string.welcome_to_yumi))
        )
        AuthViewAdapter(
            twoFactorAuthModeText = map2FAModeText(accountData.twoFactorAuthMode),
            twoFactorAuthModeActivated = map2FAModeActivation(accountData.twoFactorAuthMode),
        ).also {
            authView?.fillViewWithData(it)
        }
    }

    private fun decorateTextWithFont(
        date: String
    ) = DecoratedText(
        textToDecorate = date,
        typeface = context?.getFontFromResources(R.font.omnes_medium)
    )

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