package net.noliaware.yumi_agent.feature_alerts.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_agent.feature_alerts.domain.model.Alert

interface AlertsRepository {
    fun getAlertList(): Flow<PagingData<Alert>>
}