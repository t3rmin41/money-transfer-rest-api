package com.simple.api.exceptions

import java.lang.Exception

class AccountNotFoundException(
    val accountId: Long? = null,
    override val message: String? = null
) : Exception(message)