package com.simple.api.exceptions

import com.simple.api.domain.TransactionType
import java.lang.Exception
import java.math.BigDecimal
import java.time.Instant

data class InvalidTransactionException(
    val type: TransactionType? = null,
    val amount: BigDecimal? = null,
    val from: Long? = null,
    val to: Long? = null,
    val timestamp: Instant? = null,
    override val message: String? = null
) : Exception(message)