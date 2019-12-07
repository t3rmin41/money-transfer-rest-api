package com.revolut.api.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import java.time.Instant

data class Transaction(
    val type: TransactionType? = null,
    val from: String? = null,
    val amount: BigDecimal? = null,
    @JsonIgnore
    val timestamp: Instant? = null
)