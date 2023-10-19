package net.noliaware.yumi_agent.commun.domain.model

data class Action(
    val type: String = "",
    val params: List<ActionParam>
)