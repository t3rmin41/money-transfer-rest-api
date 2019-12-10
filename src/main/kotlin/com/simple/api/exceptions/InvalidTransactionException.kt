package com.simple.api.exceptions

import com.simple.api.domain.TransactionType
import java.time.Instant

data class InvalidTransactionException(
    val type: TransactionType? = null,
    val amount: String? = null,
    val from: Long? = null,
    val to: Long? = null,
    val timestamp: Instant? = null
)