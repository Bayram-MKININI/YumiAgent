package net.noliaware.yumi_agent.commun.util

import net.noliaware.yumi_agent.commun.domain.model.AppMessage

sealed interface Resource<T> {
    class Loading<T> : Resource<T>

    data class Success<T>(
        val data: T,
        val appMessage: AppMessage? = null
    ) : Resource<T>

    data class Error<T>(
        val appMessage: AppMessage? = null,
        val serviceError: ServiceError = ServiceError.ErrNone
    ) : Resource<T>
}