package net.noliaware.yumi_agent.feature_message.presentation.controllers

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.MESSAGE
import net.noliaware.yumi_agent.commun.domain.model.Priority
import net.noliaware.yumi_agent.commun.presentation.mappers.PriorityMapper
import net.noliaware.yumi_agent.commun.util.ViewModelState
import net.noliaware.yumi_agent.commun.util.handleSharedEvent
import net.noliaware.yumi_agent.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_agent.commun.util.withArgs
import net.noliaware.yumi_agent.feature_message.domain.model.Message
import net.noliaware.yumi_agent.feature_message.presentation.adapters.MessagePriorityAdapter
import net.noliaware.yumi_agent.feature_message.presentation.views.PriorityUI
import net.noliaware.yumi_agent.feature_message.presentation.views.SendMailView
import net.noliaware.yumi_agent.feature_message.presentation.views.SendMailView.SendMailViewCallback

@AndroidEntryPoint
class SendMailFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance(
            message: Message? = null
        ) = SendMailFragment().withArgs(MESSAGE to message)
    }

    private var sendMailView: SendMailView? = null
    private val viewModel by viewModels<SendMailFragmentViewModel>()
    var onMessageSent: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.send_mail_layout, container, false).apply {
            sendMailView = this as SendMailView
            sendMailView?.callback = sendMailViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpPriorityDropdownView()
        setUpDefaultValuesIfAny()
        collectFlows()
    }

    private fun setUpPriorityDropdownView() {
        Priority.values().map { priority ->
            val mapper = PriorityMapper()
            PriorityUI(
                resIcon = mapper.mapPriorityIcon(priority),
                label = getString(mapper.mapPriorityTitle(priority))
            )
        }.also { priorities ->
            sendMailView?.prioritySpinner?.adapter = MessagePriorityAdapter(
                requireContext(),
                priorities
            )
        }
    }

    private fun setUpDefaultValuesIfAny() {
        viewModel.message?.let { selectedMessage ->
            selectedMessage.messageSender?.let { messageSender ->
                sendMailView?.setRecipientFixed(messageSender)
            }
            sendMailView?.setSubjectFixed(selectedMessage.messageSubject)
            selectedMessage.messagePriority?.let { priority ->
                sendMailView?.setSelectedPriorityAtIndex(priority.ordinal)
            }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageSentEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.messageSentEventsHelper.stateFlow.collectLatest { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { result ->
                        if (result) {
                            dismiss()
                        }
                    }
                }
            }
        }
    }

    private val sendMailViewCallback: SendMailViewCallback by lazy {
        object : SendMailViewCallback {
            override fun onBackButtonClicked() {
                dismissAllowingStateLoss()
            }

            override fun onClearButtonClicked() {
                sendMailView?.clearMail()
            }

            override fun onSendMailClicked(
                recipients: List<String>,
                subject: String,
                text: String
            ) {
                val selectedPriorityIndex = sendMailView?.getSelectedPriorityIndex() ?: 0
                val priority = Priority.values()[selectedPriorityIndex].value

                if (viewModel.message != null) {
                    sendMailReply(priority, text)
                } else {
                    sendNewMail(recipients, subject, priority, text)
                }
            }
        }
    }

    private fun sendMailReply(priority: Int, text: String) {
        viewModel.callSendMessage(
            messagePriority = priority,
            messageId = viewModel.message?.messageId,
            messageBody = text
        )
    }

    private fun sendNewMail(
        recipients: List<String>,
        subject: String,
        priority: Int,
        text: String
    ) {
        if (recipients.isEmpty() || subject.isEmpty() || text.isEmpty()) {
            return
        }
        viewModel.callSendMessage(
            recipients = recipients,
            subject = subject,
            messagePriority = priority,
            messageBody = text
        )
    }

    override fun onResume() {
        super.onResume()
        sendMailView?.computeMailView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.messageSentEventsHelper.stateData?.let { messageSent ->
            if (messageSent) {
                onMessageSent?.invoke()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sendMailView = null
    }
}