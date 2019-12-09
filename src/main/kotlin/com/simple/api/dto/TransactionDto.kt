package com.simple.api.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.simple.api.domain.TransactionType
import java.math.BigDecimal
import java.time.Instant

data class TransactionDto(
        val type: TransactionType? = null,
        val from: String? = null,
        val to: String? = null,
        val amount: BigDecimal? = null,
        @JsonIgnore
        val timestamp: Instant? = null
)