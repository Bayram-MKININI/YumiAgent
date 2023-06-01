package net.noliaware.yumi_agent.feature_message.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.Space
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.children
import androidx.core.widget.doAfterTextChanged
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.util.convertDpToPx
import net.noliaware.yumi_agent.commun.util.inflate

class MailRecipientListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : HorizontalScrollView(context, attrs, defStyle) {

    private lateinit var contentView: LinearLayoutCompat
    private lateinit var recipientEditText: EditText

    init {
        initView()
    }

    private fun initView() {
        inflate(
            layoutRes = R.layout.mail_recipient_list_layout,
            attachToRoot = true
        )
        contentView = findViewById(R.id.content_view)

        recipientEditText = contentView.findViewById(R.id.recipient_edit_text)
        recipientEditText.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                validateMail(textView.text.toString())
            }
            false
        }

        recipientEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateMail(recipientEditText.text.toString())
            }
        }

        recipientEditText.doAfterTextChanged {
            val text = it.toString()
            if (text.lastOrNull().toString() == " ") {
                val mail = text.dropLast(1)
                validateMail(mail)
            }
        }

        setBackgroundResource(R.drawable.rectangle_rounded_grey1)
        overScrollMode = OVER_SCROLL_NEVER
    }

    private fun validateMail(mail: String) {
        if (mail.isNotEmpty()) {
            addEmailText(mail)
        }
    }

    private val mailRecipientListViewCallback by lazy {
        MailRecipientItemView.MailRecipientListViewCallback { deletedViewTag ->
            contentView.children.forEach { child ->
                child.getTag(R.string.view_tag_key)?.let { viewTag ->
                    if (viewTag.toString() == deletedViewTag) {
                        post {
                            contentView.removeView(child)
                        }
                    }
                }
            }

            post {
                if (contentView.childCount == 1) {
                    recipientEditText.hint = context.getString(R.string.recipient)
                }
            }
        }
    }

    private fun addEmailText(email: String) {

        MailRecipientItemView(context).apply {
            setText(email)
            setTag(R.string.view_tag_key, email)
            contentView.addView(this, contentView.childCount - 1)
            callback = mailRecipientListViewCallback
        }

        Space(context).apply {
            setTag(R.string.view_tag_key, email)
            minimumWidth = convertDpToPx(10)
            contentView.addView(this, contentView.childCount - 1)
        }

        recipientEditText.hint = context.getString(R.string.empty_hint)
        recipientEditText.text.clear()
    }

    fun setRecipientFixed(recipient: String) {
        recipientEditText.setText(recipient)
        recipientEditText.isEnabled = false
    }

    fun getRecipients() = contentView.children.toList().mapNotNull { child ->
        child.getTag(R.string.view_tag_key)?.toString()
    }.distinct()
}