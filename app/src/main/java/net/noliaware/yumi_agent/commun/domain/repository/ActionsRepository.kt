package net.noliaware.yumi_agent.commun.domain.repository

import net.noliaware.yumi_agent.commun.domain.model.Action

interface ActionsRepository {
    fun performActions(actions: List<Action>)
}