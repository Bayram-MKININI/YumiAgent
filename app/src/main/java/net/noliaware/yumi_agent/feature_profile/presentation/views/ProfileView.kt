package net.noliaware.yumi_agent.feature_profile.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.presentation.views.FillableTextWidget
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.getColorCompat
import net.noliaware.yumi_agent.commun.util.layoutToTopLeft
import net.noliaware.yumi_agent.commun.util.layoutToTopRight
import net.noliaware.yumi_agent.commun.util.measureWrapContent
import net.noliaware.yumi_agent.commun.util.weak
import kotlin.math.max

class ProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var myDataTextView: TextView
    private lateinit var loginTitleTextView: TextView
    private lateinit var loginValueFillableTextWidget: FillableTextWidget
    private lateinit var surnameTitleTextView: TextView
    private lateinit var surnameValueFillableTextWidget: FillableTextWidget
    private lateinit var nameTitleTextView: TextView
    private lateinit var nameValueFillableTextWidget: FillableTextWidget
    private lateinit var phoneTitleTextView: TextView
    private lateinit var phoneValueFillableTextWidget: FillableTextWidget
    private lateinit var emailTitleTextView: TextView
    private lateinit var emailValueFillableTextWidget: FillableTextWidget
    private lateinit var serviceTitleTextView: TextView
    private lateinit var serviceValueFillableTextWidget: FillableTextWidget

    private lateinit var separatorView: View
    private lateinit var myAccountsTextView: TextView
    private lateinit var usersTitleTextView: TextView
    private lateinit var usersValueFillableTextWidget: FillableTextWidget
    private lateinit var retailersTitleTextView: TextView
    private lateinit var retailersValueFillableTextWidget: FillableTextWidget
    private lateinit var partnersTitleTextView: TextView
    private lateinit var partnersValueFillableTextWidget: FillableTextWidget
    private lateinit var contributorsTitleTextView: TextView
    private lateinit var contributorsValueFillableTextWidget: FillableTextWidget
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
        loginValueFillableTextWidget = findViewById(R.id.login_value_fillable_text_view)
        loginValueFillableTextWidget.setUpValueTextView()

        surnameTitleTextView = findViewById(R.id.surname_title_text_view)
        surnameValueFillableTextWidget = findViewById(R.id.surname_value_fillable_text_view)
        surnameValueFillableTextWidget.setUpValueTextView()

        nameTitleTextView = findViewById(R.id.name_title_text_view)
        nameValueFillableTextWidget = findViewById(R.id.name_value_fillable_text_view)
        nameValueFillableTextWidget.setUpValueTextView()

        phoneTitleTextView = findViewById(R.id.phone_title_text_view)
        phoneValueFillableTextWidget = findViewById(R.id.phone_value_fillable_text_view)
        phoneValueFillableTextWidget.setUpValueTextView()

        emailTitleTextView = findViewById(R.id.email_title_text_view)
        emailValueFillableTextWidget = findViewById(R.id.email_value_fillable_text_view)
        emailValueFillableTextWidget.setUpValueTextView()

        serviceTitleTextView = findViewById(R.id.service_title_text_view)
        serviceValueFillableTextWidget = findViewById(R.id.service_value_fillable_text_view)
        serviceValueFillableTextWidget.setUpValueTextView()

        separatorView = findViewById(R.id.separator_view)
        myAccountsTextView = findViewById(R.id.my_accounts_text_view)

        usersTitleTextView = findViewById(R.id.users_title_text_view)
        usersValueFillableTextWidget = findViewById(R.id.users_value_fillable_text_view)
        usersValueFillableTextWidget.setUpValueTextView()

        retailersTitleTextView = findViewById(R.id.retailers_title_text_view)
        retailersValueFillableTextWidget = findViewById(R.id.retailers_value_fillable_text_view)
        retailersValueFillableTextWidget.setUpValueTextView()

        partnersTitleTextView = findViewById(R.id.partners_title_text_view)
        partnersValueFillableTextWidget = findViewById(R.id.partners_value_fillable_text_view)
        partnersValueFillableTextWidget.setUpValueTextView()

        contributorsTitleTextView = findViewById(R.id.contributors_title_text_view)
        contributorsValueFillableTextWidget = findViewById(R.id.contributors_value_fillable_text_view)
        contributorsValueFillableTextWidget.setUpValueTextView()

        privacyPolicyLinkTextView = findViewById(R.id.privacy_policy_link_text_view)
        privacyPolicyLinkTextView.setOnClickListener {
            callback?.onPrivacyPolicyButtonClicked()
        }
    }

    private fun FillableTextWidget.setUpValueTextView() {
        textView.apply {
            typeface = ResourcesCompat.getFont(context, R.font.omnes_semibold_regular)
            setTextColor(context.getColorCompat(R.color.grey_2))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
        }
    }

    fun fillViewWithData(profileViewAdapter: ProfileViewAdapter) {
        loginValueFillableTextWidget.setText(profileViewAdapter.login)
        surnameValueFillableTextWidget.setText(profileViewAdapter.surname)
        nameValueFillableTextWidget.setText(profileViewAdapter.name)
        phoneValueFillableTextWidget.setText(profileViewAdapter.phone)
        emailValueFillableTextWidget.setText(profileViewAdapter.email)
        serviceValueFillableTextWidget.setText(profileViewAdapter.service)

        usersValueFillableTextWidget.setText(profileViewAdapter.usersValue)
        retailersValueFillableTextWidget.setText(profileViewAdapter.retailersValue)
        partnersValueFillableTextWidget.setText(profileViewAdapter.partnersValue)
        contributorsValueFillableTextWidget.setText(profileViewAdapter.contributorsValue)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        myDataTextView.measureWrapContent()

        loginTitleTextView.measureWrapContent()
        loginValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 1 / 2)

        surnameTitleTextView.measureWrapContent()
        surnameValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 2 / 10)

        nameTitleTextView.measureWrapContent()
        nameValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 2 / 10)

        phoneTitleTextView.measureWrapContent()
        phoneValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        emailTitleTextView.measureWrapContent()
        emailValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 1 / 2)

        serviceTitleTextView.measureWrapContent()
        serviceValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 4 / 10)

        separatorView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 4 / 10, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        myAccountsTextView.measureWrapContent()

        usersTitleTextView.measureWrapContent()
        usersValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        retailersTitleTextView.measureWrapContent()
        retailersValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        partnersTitleTextView.measureWrapContent()
        partnersValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        contributorsTitleTextView.measureWrapContent()
        contributorsValueFillableTextWidget.measureTextWidgetWithWidth(viewWidth * 3 / 10)

        privacyPolicyLinkTextView.measureWrapContent()

        viewHeight = myDataTextView.measuredHeight +
                max(loginTitleTextView.measuredHeight, loginValueFillableTextWidget.measuredHeight) +
                max(surnameTitleTextView.measuredHeight, surnameValueFillableTextWidget.measuredHeight) +
                max(nameTitleTextView.measuredHeight, nameValueFillableTextWidget.measuredHeight) +
                max(phoneTitleTextView.measuredHeight, phoneValueFillableTextWidget.measuredHeight) +
                max(emailTitleTextView.measuredHeight, emailValueFillableTextWidget.measuredHeight) +
                max(serviceTitleTextView.measuredHeight, serviceValueFillableTextWidget.measuredHeight) +
                separatorView.measuredHeight + myAccountsTextView.measuredHeight +
                max(usersTitleTextView.measuredHeight, usersValueFillableTextWidget.measuredHeight) +
                max(retailersTitleTextView.measuredHeight, retailersValueFillableTextWidget.measuredHeight) +
                max(partnersTitleTextView.measuredHeight, partnersValueFillableTextWidget.measuredHeight) +
                max(
                    contributorsTitleTextView.measuredHeight,
                    contributorsValueFillableTextWidget.measuredHeight
                ) +
                privacyPolicyLinkTextView.measuredHeight + convertDpToPx(175)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    private fun View.measureTextWidgetWithWidth(width: Int) {
        measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(18), MeasureSpec.EXACTLY)
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

        loginValueFillableTextWidget.layoutToTopLeft(
            loginTitleTextView.right + convertDpToPx(15),
            loginTitleTextView.top + (loginTitleTextView.measuredHeight - loginValueFillableTextWidget.measuredHeight) / 2
        )

        surnameTitleTextView.layoutToTopRight(
            edge,
            loginTitleTextView.bottom + convertDpToPx(10)
        )

        surnameValueFillableTextWidget.layoutToTopLeft(
            surnameTitleTextView.right + convertDpToPx(15),
            surnameTitleTextView.top + (surnameTitleTextView.measuredHeight - surnameValueFillableTextWidget.measuredHeight) / 2
        )

        nameTitleTextView.layoutToTopRight(
            edge,
            surnameTitleTextView.bottom + convertDpToPx(10)
        )

        nameValueFillableTextWidget.layoutToTopLeft(
            nameTitleTextView.right + convertDpToPx(15),
            nameTitleTextView.top + (nameTitleTextView.measuredHeight - nameValueFillableTextWidget.measuredHeight) / 2
        )

        phoneTitleTextView.layoutToTopRight(
            edge,
            nameValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        phoneValueFillableTextWidget.layoutToTopLeft(
            phoneTitleTextView.right + convertDpToPx(15),
            phoneTitleTextView.top + (phoneTitleTextView.measuredHeight - phoneValueFillableTextWidget.measuredHeight) / 2
        )

        emailTitleTextView.layoutToTopRight(
            edge,
            phoneValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        emailValueFillableTextWidget.layoutToTopLeft(
            emailTitleTextView.right + convertDpToPx(15),
            emailTitleTextView.top + (emailTitleTextView.measuredHeight - emailValueFillableTextWidget.measuredHeight) / 2
        )

        serviceTitleTextView.layoutToTopRight(
            edge,
            emailValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        serviceValueFillableTextWidget.layoutToTopLeft(
            serviceTitleTextView.right + convertDpToPx(15),
            serviceTitleTextView.top + (serviceTitleTextView.measuredHeight - serviceValueFillableTextWidget.measuredHeight) / 2
        )

        separatorView.layoutToTopLeft(
            (viewWidth - separatorView.measuredWidth) / 2,
            serviceValueFillableTextWidget.bottom + convertDpToPx(20)
        )

        myAccountsTextView.layoutToTopLeft(
            myDataTextView.left,
            separatorView.bottom + convertDpToPx(15)
        )

        usersTitleTextView.layoutToTopRight(
            edge,
            myAccountsTextView.bottom + convertDpToPx(10)
        )

        usersValueFillableTextWidget.layoutToTopLeft(
            usersTitleTextView.right + convertDpToPx(15),
            usersTitleTextView.top + (usersTitleTextView.measuredHeight - usersValueFillableTextWidget.measuredHeight) / 2
        )

        retailersTitleTextView.layoutToTopRight(
            edge,
            usersValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        retailersValueFillableTextWidget.layoutToTopLeft(
            retailersTitleTextView.right + convertDpToPx(15),
            retailersTitleTextView.top + (retailersTitleTextView.measuredHeight - retailersValueFillableTextWidget.measuredHeight) / 2
        )

        partnersTitleTextView.layoutToTopRight(
            edge,
            retailersValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        partnersValueFillableTextWidget.layoutToTopLeft(
            partnersTitleTextView.right + convertDpToPx(15),
            partnersTitleTextView.top + (partnersTitleTextView.measuredHeight - partnersValueFillableTextWidget.measuredHeight) / 2
        )

        contributorsTitleTextView.layoutToTopRight(
            edge,
            partnersValueFillableTextWidget.bottom + convertDpToPx(10)
        )

        contributorsValueFillableTextWidget.layoutToTopLeft(
            contributorsTitleTextView.right + convertDpToPx(15),
            contributorsTitleTextView.top + (contributorsTitleTextView.measuredHeight - contributorsValueFillableTextWidget.measuredHeight) / 2
        )

        privacyPolicyLinkTextView.layoutToTopLeft(
            (viewWidth - privacyPolicyLinkTextView.measuredWidth) / 2,
            contributorsTitleTextView.bottom + convertDpToPx(30)
        )
    }
}