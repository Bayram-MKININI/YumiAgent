package net.noliaware.yumi_agent.feature_auth.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.GOLDEN_RATIO
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.getStatusBarHeight
import net.noliaware.yumi_agent.commun.util.layoutToBottomLeft
import net.noliaware.yumi_agent.commun.util.layoutToTopLeft
import net.noliaware.yumi_agent.commun.util.measureWrapContent
import net.noliaware.yumi_agent.commun.util.weak
import kotlin.math.roundToInt

class AuthView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var headerView: View
    private lateinit var helloTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var lastLoginTitleTextView: TextView
    private lateinit var lastLoginValueTextView: TextView
    private lateinit var boAccessImageView: View
    private lateinit var boAccessTextView: TextView
    private lateinit var boAccessDescriptionTextView: TextView
    private lateinit var accessButtonLayout: LinearLayoutCompat
    var callback: AuthViewCallback? by weak()

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
        nameTextView = findViewById(R.id.name_text_view)
        lastLoginTitleTextView = findViewById(R.id.last_login_title_text_view)
        lastLoginValueTextView = findViewById(R.id.last_login_value_text_view)
        boAccessImageView = findViewById(R.id.bo_access_image_view)
        boAccessTextView = findViewById(R.id.bo_access_text_view)
        boAccessDescriptionTextView = findViewById(R.id.bo_access_description_text_view)
        accessButtonLayout = findViewById(R.id.access_button_layout)
        accessButtonLayout.setOnClickListener { callback?.onAuthClicked() }
    }

    fun setUserData(
        helloText: String,
        userName: String,
        lastLogin: String
    ) {
        helloTextView.text = helloText
        nameTextView.text = userName
        lastLoginValueTextView.text = lastLogin
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

        val nameTextViewWidth = viewWidth - (helloTextView.measuredWidth + convertDpToPx(35))
        nameTextView.measure(
            MeasureSpec.makeMeasureSpec(nameTextViewWidth, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        lastLoginTitleTextView.measureWrapContent()

        lastLoginValueTextView.measureWrapContent()

        boAccessTextView.measureWrapContent()

        boAccessDescriptionTextView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth * 9 / 10, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        accessButtonLayout.measureWrapContent()

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

        nameTextView.layoutToTopLeft(
            helloTextView.right + convertDpToPx(5),
            helloTextView.top
        )

        lastLoginTitleTextView.layoutToTopLeft(
            helloTextView.left,
            helloTextView.bottom + convertDpToPx(5)
        )

        lastLoginValueTextView.layoutToBottomLeft(
            lastLoginTitleTextView.right + convertDpToPx(2),
            lastLoginTitleTextView.bottom
        )

        val contentHeight = boAccessImageView.measuredHeight + boAccessTextView.measuredHeight + boAccessDescriptionTextView.measuredHeight +
                accessButtonLayout.measuredHeight + getStatusBarHeight() + convertDpToPx(40)

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