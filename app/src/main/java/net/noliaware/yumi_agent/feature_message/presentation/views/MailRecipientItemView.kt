package net.noliaware.yumi_agent.feature_message.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.inflate
import net.noliaware.yumi_agent.commun.util.layoutToTopLeft
import net.noliaware.yumi_agent.commun.util.measureWrapContent
import net.noliaware.yumi_agent.commun.util.weak

class MailRecipientItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

    private lateinit var contentTextView: TextView
    private lateinit var closeImageView: ImageView

    var callback: MailRecipientListViewCallback? by weak()

    fun interface MailRecipientListViewCallback {
        fun onCloseButtonClicked(tag: String)
    }


    init {
        initView()
    }

    private fun initView() {
        inflate(
            layoutRes = R.layout.mail_recipient_item_layout,
            attachToRoot = true
        )
        contentTextView = findViewById(R.id.content_text_view)
        closeImageView = findViewById(R.id.close_image_view)
        closeImageView.setOnClickListener {
            callback?.onCloseButtonClicked(getTag(R.string.view_tag_key).toString())
        }
        setBackgroundResource(R.drawable.rectangle_rounded_corners_empty_20dp)
    }

    fun setText(title: String) {
        contentTextView.text = title
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        closeImageView.setOnClickListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        contentTextView.measureWrapContent()
        closeImageView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(18), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(18), MeasureSpec.EXACTLY)
        )

        val viewWidth = contentTextView.measuredWidth + closeImageView.measuredWidth +
                convertDpToPx(30)
        val viewHeight = contentTextView.measuredHeight + convertDpToPx(10)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val viewWidth = right - left
        val viewHeight = bottom - top

        contentTextView.layoutToTopLeft(
            convertDpToPx(10),
            (viewHeight - contentTextView.measuredHeight) / 2,
        )

        closeImageView.layoutToTopLeft(
            contentTextView.right + convertDpToPx(10),
            (viewHeight - closeImageView.measuredHeight) / 2,
        )
    }
}