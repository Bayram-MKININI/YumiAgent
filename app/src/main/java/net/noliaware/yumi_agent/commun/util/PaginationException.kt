package net.noliaware.yumi_agent.commun.util

class PaginationException(val serviceError: ServiceError) : Exception(serviceError.toString())