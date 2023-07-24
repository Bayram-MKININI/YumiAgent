package net.noliaware.yumi_agent.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.layoutToTopLeft
import net.noliaware.yumi_agent.commun.util.layoutToTopRight
import net.noliaware.yumi_agent.commun.util.measureWrapContent
import net.noliaware.yumi_agent.commun.util.weak

class ProfileView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var myDataTextView: TextView
    private lateinit var loginTitleTextView: TextView
    private lateinit var loginValueTextView: TextView
    private lateinit var surnameTitleTextView: TextView
    private lateinit var surnameValueTextView: TextView
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameValueTextView: TextView
    private lateinit var phoneTitleTextView: TextView
    private lateinit var phoneValueTextView: TextView
    private lateinit var emailTitleTextView: TextView
    private lateinit var emailValueTextView: TextView
    private lateinit var serviceTitleTextView: TextView
    private lateinit var serviceValueTextView: TextView

    private lateinit var separatorView: View
    private lateinit var myAccountsTextView: TextView
    private lateinit var usersTitleTextView: TextView
    private lateinit var usersValueTextView: TextView
    private lateinit var retailersTitleTextView: TextView
    private lateinit var retailersValueTextView: TextView
    private lateinit var partnersTitleTextView: TextView
    private lateinit var partnersValueTextView: TextView
    private lateinit var contributorsTitleTextView: TextView
    private lateinit var contributorsValueTextView: TextView
    private lateinit var privacyPolicyLinkTextView: TextView
    var callback: ProfileViewCallback? by weak()

    data class ProfileViewAdapter(
        val login: String = "",
        val surname: String = "",
        val name: String = "",
        val phone: String = "",
        val email: String = "",
        val service: String = "",
        val usersValue: String = "",
        val retailersValue: String = "",
        val partnersValue: String = "",
        val contributorsValue: String = ""
    )

    fun interface ProfileViewCallback {
        fun onPrivacyPolicyButtonClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        myDataTextView = findViewById(R.id.my_data_text_view)
        loginTitleTextView = findViewById(R.id.login_title_text_view)
        loginValueTextView = findViewById(R.id.login_value_text_view)
        surnameTitleTextView = findViewById(R.id.surname_title_text_view)
        surnameValueTextView = findViewById(R.id.surname_value_text_view)
        nameTitleTextView = findViewById(R.id.name_title_text_view)
        nameValueTextView = findViewById(R.id.name_value_text_view)
        phoneTitleTextView = findViewById(R.id.phone_title_text_view)
        phoneValueTextView = findViewById(R.id.phone_value_text_view)
        emailTitleTextView = findViewById(R.id.email_title_text_view)
        emailValueTextView = findViewById(R.id.email_value_text_view)
        serviceTitleTextView = findViewById(R.id.service_title_text_view)
        serviceValueTextView = findViewById(R.id.service_value_text_view)

        separatorView = findViewById(R.id.separator_view)
        myAccountsTextView = findViewById(R.id.my_accounts_text_view)
        usersTitleTextView = findViewById(R.id.users_title_text_view)
        usersValueTextView = findViewById(R.id.users_value_text_view)
        retailersTitleTextView = findViewById(R.id.retailers_title_text_view)
        retailersValueTextView = findViewById(R.id.retailers_value_text_view)
        partnersTitleTextView = findViewById(R.id.partners_title_text_view)
        partnersValueTextView = findViewById(R.id.partners_value_text_view)
        contributorsTitleTextView = findViewById(R.id.contributors_title_text_view)
        contributorsValueTextView = findViewById(R.id.contributors_value_text_view)

        privacyPolicyLinkTextView = findViewById(R.id.privacy_policy_link_text_view)
        privacyPolicyLinkTextView.setOnClickListener {
            callback?.onPrivacyPolicyButtonClicked()
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {
        loginValueTextView.text = profileViewAdapter.login
        surnameValueTextView.text = profileViewAdapter.surname
        nameValueTextView.text = profileViewAdapter.name
        phoneValueTextView.text = profileViewAdapter.phone
        emailValueTextView.text = profileViewAdapter.email
        serviceValueTextView.text = profileViewAdapter.service

        usersValueTextView.text = profileViewAdapter.usersValue
        retailersValueTextView.text = profileViewAdapter.retailersValue
        partnersValueTextView.text = profileViewAdapter.partnersValue
        contributorsValueTextView.text = profileViewAdapter.contributorsValue
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myDataTextView.measureWrapContent()

        loginTitleTextView.measureWrapContent()
        loginValueTextView.measureWrapContent()

        surnameTitleTextView.measureWrapContent()
        surnameValueTextView.measureWrapContent()

        nameTitleTextView.measureWrapContent()
        nameValueTextView.measureWrapContent()

        phoneTitleTextView.measureWrapContent()
        phoneValueTextView.measureWrapContent()

        emailTitleTextView.measureWrapContent()
        emailValueTextView.measureWrapContent()

        serviceTitleTextView.measureWrapContent()
        serviceValueTextView.measureWrapContent()

        separatorView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        myAccountsTextView.measureWrapContent()

        usersTitleTextView.measureWrapContent()
        usersValueTextView.measureWrapContent()

        retailersTitleTextView.measureWrapContent()
        retailersValueTextView.measureWrapContent()

        partnersTitleTextView.measureWrapContent()
        partnersValueTextView.measureWrapContent()

        contributorsTitleTextView.measureWrapContent()
        contributorsValueTextView.measureWrapContent()

        privacyPolicyLinkTextView.measureWrapContent()

        viewHeight = myDataTextView.measuredHeight + loginValueTextView.measuredHeight +
                surnameValueTextView.measuredHeight + nameValueTextView.measuredHeight +
                phoneValueTextView.measuredHeight + emailValueTextView.measuredHeight +
                serviceValueTextView.measuredHeight + separatorView.measuredHeight +
                myAccountsTextView.measuredHeight + usersValueTextView.measuredHeight +
                retailersValueTextView.measuredHeight + partnersValueTextView.measuredHeight +
                contributorsValueTextView.measuredHeight + privacyPolicyLinkTextView.measuredHeight +
                convertDpToPx(175)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        myDataTextView.layoutToTopLeft(
            convertDpToPx(20),
            0
        )

        val edge = viewWidth * 1 / 3

        loginTitleTextView.layoutToTopRight(
            edge,
            myDataTextView.bottom + convertDpToPx(15)
        )

        loginValueTextView.layoutToTopLeft(
            loginTitleTextView.right + convertDpToPx(15),
            loginTitleTextView.top
        )

        surnameTitleTextView.layoutToTopRight(
            edge,
            loginTitleTextView.bottom + convertDpToPx(10)
        )

        surnameValueTextView.layoutToTopLeft(
            surnameTitleTextView.right + convertDpToPx(15),
            surnameTitleTextView.top
        )

        nameTitleTextView.layoutToTopRight(
            edge,
            surnameTitleTextView.bottom + convertDpToPx(10)
        )

        nameValueTextView.layoutToTopLeft(
            nameTitleTextView.right + convertDpToPx(15),
            nameTitleTextView.top
        )

        phoneTitleTextView.layoutToTopRight(
            edge,
            nameValueTextView.bottom + convertDpToPx(10)
        )

        phoneValueTextView.layoutToTopLeft(
            phoneTitleTextView.right + convertDpToPx(15),
            phoneTitleTextView.top
        )

        emailTitleTextView.layoutToTopRight(
            edge,
            phoneValueTextView.bottom + convertDpToPx(10)
        )

        emailValueTextView.layoutToTopLeft(
            emailTitleTextView.right + convertDpToPx(15),
            emailTitleTextView.top
        )

        serviceTitleTextView.layoutToTopRight(
            edge,
            emailValueTextView.bottom + convertDpToPx(10)
        )

        serviceValueTextView.layoutToTopLeft(
            serviceTitleTextView.right + convertDpToPx(15),
            serviceTitleTextView.top
        )

        separatorView.layoutToTopLeft(
            (viewWidth - separatorView.measuredWidth) / 2,
            serviceValueTextView.bottom + convertDpToPx(20)
        )

        myAccountsTextView.layoutToTopLeft(
            myDataTextView.left,
            separatorView.bottom + convertDpToPx(15)
        )

        usersTitleTextView.layoutToTopRight(
            edge,
            myAccountsTextView.bottom + convertDpToPx(10)
        )

        usersValueTextView.layoutToTopLeft(
            usersTitleTextView.right + convertDpToPx(15),
            usersTitleTextView.top
        )

        retailersTitleTextView.layoutToTopRight(
            edge,
            usersValueTextView.bottom + convertDpToPx(10)
        )

        retailersValueTextView.layoutToTopLeft(
            retailersTitleTextView.right + convertDpToPx(15),
            retailersTitleTextView.top
        )

        partnersTitleTextView.layoutToTopRight(
            edge,
            retailersValueTextView.bottom + convertDpToPx(10)
        )

        partnersValueTextView.layoutToTopLeft(
            partnersTitleTextView.right + convertDpToPx(15),
            partnersTitleTextView.top
        )

        contributorsTitleTextView.layoutToTopRight(
            edge,
            partnersValueTextView.bottom + convertDpToPx(10)
        )

        contributorsValueTextView.layoutToTopLeft(
            contributorsTitleTextView.right + convertDpToPx(15),
            contributorsTitleTextView.top
        )

        privacyPolicyLinkTextView.layoutToTopLeft(
            (viewWidth - privacyPolicyLinkTextView.measuredWidth) / 2,
            contributorsTitleTextView.bottom + convertDpToPx(30)
        )
    }
}