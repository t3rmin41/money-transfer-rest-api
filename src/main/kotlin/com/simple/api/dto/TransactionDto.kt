package com.simple.api.dto

import com.simple.api.domain.TransactionType
import java.math.BigDecimal
import java.time.Instant

data class TransactionDto(
    val id: Long? = null,
    val type: String? = null,
    val from: Long? = null,
    val to: Long? = null,
    val amount: BigDecimal? = null,
    val timestamp: Instant? = null
)