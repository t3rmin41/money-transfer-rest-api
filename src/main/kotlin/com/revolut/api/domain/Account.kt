package com.revolut.api.domain

import java.math.BigDecimal

data class Account(
    val id: String? = null,
    val name: String? = null,
    val balance: BigDecimal? = null
)