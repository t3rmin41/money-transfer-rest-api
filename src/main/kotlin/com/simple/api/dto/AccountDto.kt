package com.simple.api.dto

import java.math.BigDecimal

data class AccountDto(
    val id: Long,
    val name: String? = null,
    val balance: BigDecimal? = null
)