package com.simple.api.domain

import java.math.BigDecimal
import java.time.Instant

data class Transaction(
    val type: TransactionType? = null,
    val from: String? = null,
    val to: String? = null,
    val amount: BigDecimal? = null,
    val timestamp: Instant? = null
)