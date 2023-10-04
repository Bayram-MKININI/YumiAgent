package net.noliaware.yumi_agent.feature_message.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_agent.R
import net.noliaware.yumi_agent.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi_agent.commun.util.handlePaginationError
import net.noliaware.yumi_agent.commun.util.safeNavigate
import net.noliaware.yumi_agent.feature_message.presentation.adapters.MessageAdapter
import net.noliaware.yumi_agent.feature_message.presentation.mappers.ReceivedMessageMapper
import net.noliaware.yumi_agent.feature_message.presentation.views.MessagesListView

@AndroidEntryPoint
class ReceivedMessagesFragment : Fragment() {

    private var messagesListView: MessagesListView? = null
    private val viewModel by viewModels<MessagingFragmentViewModel>(
        ownerProducer = {
            requireParentFragment()
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.messages_list_layout, container, false).apply {
            messagesListView = this as MessagesListView
            messagesListView?.messageAdapter = MessageAdapter(ReceivedMessageMapper()) { message ->
                findNavController().safeNavigate(
                    MessagingFragmentDirections.actionMessagingFragmentToReadInboxMailFragment(
                        message.messageId
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messagesListView?.setLoadingVisible(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.onReceivedListRefreshedEventFlow.collectLatest {
                messagesListView?.messageAdapter?.refresh()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            messagesListView?.messageAdapter?.loadStateFlow?.collectLatest { loadState ->

                val noMessagesLoaded = (messagesListView?.messageAdapter?.itemCount ?: 0) < 1
                when {
                    handlePaginationError(loadState) -> messagesListView?.stopLoading()
                    loadState.refresh is LoadState.NotLoading -> {
                        messagesListView?.setLoadingVisible(false)
                        messagesListView?.setEmptyMessageText(getString(R.string.no_received_message))
                        messagesListView?.setEmptyMessageVisible(noMessagesLoaded)
                    }

                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getReceivedMessages().collectLatest {
                messagesListView?.messageAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                messagesListView?.messageAdapter?.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        messagesListView = null
        super.onDestroyView()
    }
}