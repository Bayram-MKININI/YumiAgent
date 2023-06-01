package net.noliaware.yumi_agent.commun.util

class PaginationException(val errorType: ErrorType) : Exception(errorType.toString())
