package net.noliaware.yumi_agent.feature_message.presentation.views

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.getStatusBarHeight
import net.noliaware.yumi_agent.commun.util.layoutToBottomRight
import net.noliaware.yumi_agent.commun.util.layoutToTopLeft
import net.noliaware.yumi_agent.commun.util.layoutToTopRight
import net.noliaware.yumi_agent.commun.util.measureWrapContent
import net.noliaware.yumi_agent.commun.util.weak
import kotlin.math.max


class SendMailView(context: Context, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    private lateinit var backgroundView: View
    private lateinit var headerView: View
    private lateinit var messageIconView: View
    private lateinit var backView: View
    private lateinit var contentView: View
    private lateinit var titleTextView: TextView

    private lateinit var mailRecipientListView: MailRecipientListView

    private lateinit var messageBackgroundView: View
    private lateinit var subjectEditText: EditText
    private lateinit var clearSubjectImageView: ImageView

    lateinit var prioritySpinner: Spinner
        private set
    private lateinit var fixedPriorityImageView: ImageView

    private lateinit var separatorLineView: View
    private lateinit var messageParentLayout: View
    private lateinit var mailEditText: EditText
    private lateinit var sendButton: View
    private val visibleRect = Rect()
    var callback: SendMailViewCallback? by weak()

    interface SendMailViewCallback {
        fun onBackButtonClicked()
        fun onClearButtonClicked()
        fun onSendMailClicked(recipients: List<String>, subject: String, text: String)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {

        backgroundView = findViewById(R.id.background_view)
        headerView = findViewById(R.id.header_view)
        messageIconView = findViewById(R.id.message_icon_view)
        backView = findViewById(R.id.back_view)
        backView.setOnClickListener(onClickListener)
        contentView = findViewById(R.id.content_layout)

        titleTextView = contentView.findViewById(R.id.title_text_view)

        mailRecipientListView = contentView.findViewById(R.id.mail_recipient_list_view)

        messageBackgroundView = contentView.findViewById(R.id.message_background)
        subjectEditText = contentView.findViewById(R.id.subject_edit_text)
        clearSubjectImageView = contentView.findViewById(R.id.clear_subject_image_view)
        clearSubjectImageView.setOnClickListener { subjectEditText.text.clear() }

        prioritySpinner = contentView.findViewById(R.id.priority_spinner)
        fixedPriorityImageView = contentView.findViewById(R.id.fixed_priority_image_view)

        separatorLineView = contentView.findViewById(R.id.separator_line_view)
        messageParentLayout = contentView.findViewById(R.id.message_parent_layout)
        mailEditText = messageParentLayout.findViewById(R.id.mail_edit_text)

        sendButton = findViewById(R.id.send_icon_view)
        sendButton.setOnClickListener(onClickListener)

        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                messageParentLayout.requestLayout()
            }
            insets
        }
    }

    private val onClickListener: OnClickListener by lazy {
        OnClickListener {
            when (it.id) {
                R.id.back_view -> callback?.onBackButtonClicked()
                R.id.send_icon_view -> callback?.onSendMailClicked(
                    mailRecipientListView.getRecipients(),
                    subjectEditText.text.toString(),
                    mailEditText.text.toString()
                )
            }
        }
    }

    fun setMailDomain(mailDomain: String) {
        mailRecipientListView.mailDomain = mailDomain
    }

    fun setRecipientFixed(recipient: String) {
        mailRecipientListView.setRecipientFixed(recipient)
    }

    fun setSubjectFixed(subject: String) {
        subjectEditText.setText(subject)
        subjectEditText.isEnabled = false
        clearSubjectImageView.isGone = true
    }

    fun getSelectedPriorityIndex() = prioritySpinner.selectedItemPosition

    fun setPriorityFixed(@DrawableRes subjectIcon: Int) {
        prioritySpinner.isGone = true
        fixedPriorityImageView.isVisible = true
        fixedPriorityImageView.setImageResource(subjectIcon)
    }

    fun computeMailView() {
        post {
            mailEditText.requestLayout()
        }
    }

    fun clearMail() {
        mailEditText.text.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        backgroundView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight - getStatusBarHeight(), MeasureSpec.EXACTLY)
        )

        headerView.measure(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                getStatusBarHeight() + convertDpToPx(75),
                MeasureSpec.EXACTLY
            )
        )

        backView.measureWrapContent()
        messageIconView.measure(
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY)
        )

        val contentViewHeight = viewHeight - (headerView.measuredHeight + messageIconView.measuredHeight / 2 +
                    convertDpToPx(25))

        val contentViewWidth = viewWidth * 95 / 100
        contentView.measure(
            MeasureSpec.makeMeasureSpec(contentViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY)
        )

        titleTextView.measureWrapContent()

        getWindowVisibleDisplayFrame(visibleRect)

        sendButton.measureWrapContent()

        val screenHeight = viewHeight - getStatusBarHeight()
        val messageBackgroundViewWidth = contentView.measuredWidth * 95 / 100

        mailRecipientListView.measure(
            MeasureSpec.makeMeasureSpec(messageBackgroundViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(48), MeasureSpec.EXACTLY)
        )

        val messageBackgroundViewHeight = contentView.measuredHeight - (titleTextView.measuredHeight + mailRecipientListView.measuredHeight +
                    sendButton.measuredHeight / 2 +
                    if (visibleRect.height() == screenHeight) {
                        convertDpToPx(50)
                    } else {
                        contentView.measuredHeight - visibleRect.height() + convertDpToPx(40) + (viewWidth * 5 / 200)
                    })

        messageBackgroundView.measure(
            MeasureSpec.makeMeasureSpec(messageBackgroundViewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(messageBackgroundViewHeight, MeasureSpec.EXACTLY)
        )

        clearSubjectImageView.measureWrapContent()

        if (prioritySpinner.isVisible) {
            prioritySpinner.measure(
                MeasureSpec.makeMeasureSpec(convertDpToPx(50), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
        }

        if (fixedPriorityImageView.isVisible) {
            fixedPriorityImageView.measureWrapContent()
        }

        val separatorWidth = messageBackgroundView.measuredWidth - convertDpToPx(30)

        val objectEditTextWidth = separatorWidth - (
                max(
                    prioritySpinner.measuredWidth,
                    fixedPriorityImageView.measuredWidth
                ) + convertDpToPx(5)
                ) - if (clearSubjectImageView.isVisible) {
            clearSubjectImageView.measuredWidth + convertDpToPx(5)
        } else {
            0
        }

        subjectEditText.measure(
            MeasureSpec.makeMeasureSpec(objectEditTextWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        separatorLineView.measure(
            MeasureSpec.makeMeasureSpec(separatorWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(convertDpToPx(3), MeasureSpec.EXACTLY)
        )

        val availableHeightForBody = messageBackgroundView.measuredHeight - (
                listOf(
                    subjectEditText.measuredHeight + convertDpToPx(10),
                    prioritySpinner.measuredHeight + convertDpToPx(4),
                    fixedPriorityImageView.measuredHeight + convertDpToPx(16)
                ).max() + separatorLineView.measuredHeight + convertDpToPx(20))

        mailEditText.minHeight = availableHeightForBody

        messageParentLayout.measure(
            MeasureSpec.makeMeasureSpec(separatorWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(availableHeightForBody, MeasureSpec.EXACTLY)
        )

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val viewWidth = right - left
        val viewHeight = bottom - top
        val screenHeight = viewHeight - getStatusBarHeight()

        backgroundView.layoutToTopLeft(
            0,
            getStatusBarHeight()
        )

        headerView.layoutToTopLeft(0, 0)

        backView.layoutToTopLeft(
            convertDpToPx(10),
            getStatusBarHeight() + convertDpToPx(10)
        )

        messageIconView.layoutToTopLeft(
            (viewWidth - messageIconView.measuredWidth) / 2,
            headerView.bottom - messageIconView.measuredHeight / 2
        )

        val contentSideSpace = (viewWidth - contentView.measuredWidth) / 2
        val contentViewTop = if (visibleRect.height() == screenHeight) {
            messageIconView.bottom + convertDpToPx(15)
        } else {
            getStatusBarHeight() + contentSideSpace
        }
        contentView.layoutToTopLeft(
            contentSideSpace,
            contentViewTop
        )

        titleTextView.layoutToTopLeft(
            (contentView.measuredWidth - titleTextView.measuredWidth) / 2,
            convertDpToPx(15)
        )

        mailRecipientListView.layoutToTopLeft(
            (contentView.measuredWidth - mailRecipientListView.measuredWidth) / 2,
            titleTextView.bottom + convertDpToPx(15)
        )

        messageBackgroundView.layoutToTopLeft(
            (contentView.measuredWidth - messageBackgroundView.measuredWidth) / 2,
            mailRecipientListView.bottom + convertDpToPx(10)
        )

        val spaceForMessageObject = listOf(
            subjectEditText.measuredHeight + convertDpToPx(10),
            prioritySpinner.measuredHeight + convertDpToPx(4),
            fixedPriorityImageView.measuredHeight + convertDpToPx(16)
        ).max()

        subjectEditText.layoutToTopLeft(
            messageBackgroundView.left + convertDpToPx(15),
            messageBackgroundView.top + (spaceForMessageObject - subjectEditText.measuredHeight) / 2
        )

        clearSubjectImageView.layoutToTopLeft(
            subjectEditText.right + convertDpToPx(5),
            messageBackgroundView.top + (spaceForMessageObject - clearSubjectImageView.measuredHeight) / 2
        )

        if (prioritySpinner.isVisible) {
            prioritySpinner.layoutToTopRight(
                messageBackgroundView.right - convertDpToPx(15),
                messageBackgroundView.top + (spaceForMessageObject - prioritySpinner.measuredHeight) / 2
            )
        }
        if (fixedPriorityImageView.isVisible) {
            fixedPriorityImageView.layoutToTopRight(
                messageBackgroundView.right - convertDpToPx(15),
                messageBackgroundView.top + (spaceForMessageObject - fixedPriorityImageView.measuredHeight) / 2
            )
        }

        separatorLineView.layoutToTopLeft(
            (contentView.measuredWidth - separatorLineView.measuredWidth) / 2,
            messageBackgroundView.top + spaceForMessageObject
        )

        messageParentLayout.layoutToTopLeft(
            (contentView.measuredWidth - messageParentLayout.measuredWidth) / 2,
            separatorLineView.bottom
        )

        sendButton.layoutToBottomRight(
            messageBackgroundView.right,
            messageBackgroundView.bottom + sendButton.measuredHeight / 2
        )
    }
}