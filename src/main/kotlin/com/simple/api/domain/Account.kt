package com.simple.api.domain

import java.math.BigDecimal

data class Account(
    val id: Long?,
    var name: String? = null,
    var balance: BigDecimal? = BigDecimal.ZERO
)