package net.noliaware.yumi_agent.feature_auth.presentation.views

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.UI.GOLDEN_RATIO
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.getStatusBarHeight
import net.noliaware.yumi_agent.commun.util.layoutToTopLeft
import net.noliaware.yumi_agent.commun.util.measureWrapContent
import net.noliaware.yumi_agent.commun.util.sizeForVisible
import kotlin.math.roundToInt

class AuthView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var headerView: View
    private lateinit var helloTextView: TextView
    private lateinit var lastLoginTextView: TextView
    private lateinit var boAccessImageView: View
    private lateinit var boAccessTextView: TextView
    private lateinit var boAccessDescriptionTextView: TextView
    private lateinit var accessButtonLayout: LinearLayoutCompat
    var callback: AuthViewCallback? = null

    data class AuthViewAdapter(
        val twoFactorAuthModeText: String = "",
        val twoFactorAuthModeActivated: Boolean = false
    )

    fun interface AuthViewCallback {
        fun onAuthClicked()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        headerView = findViewById(R.id.header_view)
        helloTextView = findViewById(R.id.hello_text_view)
        lastLoginTextView = findViewById(R.id.last_login_text_view)
        boAccessImageView = findViewById(R.id.bo_access_image_view)
        boAccessTextView = findViewById(R.id.bo_access_text_view)
        boAccessDescriptionTextView = findViewById(R.id.bo_access_description_text_view)
        accessButtonLayout = findViewById(R.id.access_button_layout)
        accessButtonLayout.setOnClickListener {
            callback?.onAuthClicked()
        }
    }

    fun fillViewWithData(authViewAdapter: AuthViewAdapter) {
        boAccessDescriptionTextView.text = authViewAdapter.twoFactorAuthModeText
        accessButtonLayout.isVisible = authViewAdapter.twoFactorAuthModeActivated
    }

    fun setUserData(
        helloText: SpannableString,
        lastLogin: SpannableString
    ) {
        helloTextView.text = helloText
        lastLoginTextView.text = lastLogin
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(100), MeasureSpec.EXACTLY
            )
        )

        helloTextView.measureWrapContent()
        lastLoginTextView.measureWrapContent()
        boAccessTextView.measureWrapContent()

        boAccessDescriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        if (accessButtonLayout.isVisible) {
            accessButtonLayout.measureWrapContent()
        }

        boAccessImageView.measure(
            MeasureSpec.makeMeasureSpec(
                (boAccessTextView.measuredWidth / GOLDEN_RATIO).roundToInt(),
                MeasureSpec.EXACTLY
            ),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        headerView.layoutToTopLeft(0, 0)

        helloTextView.layoutToTopLeft(
            convertDpToPx(15),
            getStatusBarHeight() + convertDpToPx(10)
        )

        lastLoginTextView.layoutToTopLeft(
            helloTextView.left,
            helloTextView.bottom + convertDpToPx(5)
        )

        val contentHeight = getStatusBarHeight() + boAccessImageView.measuredHeight +
                boAccessTextView.measuredHeight + boAccessDescriptionTextView.measuredHeight +
                accessButtonLayout.sizeForVisible {
                    accessButtonLayout.measuredHeight + convertDpToPx(15)
                } + convertDpToPx(25)

        boAccessImageView.layoutToTopLeft(
            (viewWidth - boAccessImageView.measuredWidth) / 2,
            (viewHeight / GOLDEN_RATIO - contentHeight / 2).roundToInt()
        )

        boAccessTextView.layoutToTopLeft(
            (viewWidth - boAccessTextView.measuredWidth) / 2,
            boAccessImageView.bottom + convertDpToPx(15)
        )

        boAccessDescriptionTextView.layoutToTopLeft(
            (viewWidth - boAccessDescriptionTextView.measuredWidth) / 2,
            boAccessTextView.bottom + convertDpToPx(10)
        )

        accessButtonLayout.layoutToTopLeft(
            (viewWidth - accessButtonLayout.measuredWidth) / 2,
            boAccessDescriptionTextView.bottom + convertDpToPx(15)
        )
    }
}