package com.simple.api.domain

import java.math.BigDecimal
import java.time.Instant

data class Transaction(
    val id: Long? = null,
    val type: TransactionType? = null,
    val from: Long? = null,
    val to: Long? = null,
    val amount: BigDecimal? = null,
    val timestamp: Instant? = null
)