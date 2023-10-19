package net.noliaware.yumi_agent.commun.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.noliaware.yumi_agent.commun.ActionTypes.DELETE_CACHED_DEVICE_ID
import net.noliaware.yumi_agent.commun.ActionTypes.MONITOR
import net.noliaware.yumi_agent.commun.data.remote.RemoteApi
import net.noliaware.yumi_agent.commun.domain.model.Action
import net.noliaware.yumi_agent.commun.domain.model.SessionData
import net.noliaware.yumi_agent.commun.domain.repository.ActionsRepository
import net.noliaware.yumi_agent.feature_login.domain.repository.DataStoreRepository
import javax.inject.Inject

class ActionsRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData,
    private val dataStoreRepository: DataStoreRepository
) : ActionsRepository {

    override fun performActions(actions: List<Action>) {
        GlobalScope.launch {
            actions.forEach {
                when (it.type) {
                    DELETE_CACHED_DEVICE_ID -> {
                        withContext(Dispatchers.IO) {
                            dataStoreRepository.clearDataStore()
                        }
                    }

                    MONITOR -> {
                    }
                }
            }
        }
    }
}