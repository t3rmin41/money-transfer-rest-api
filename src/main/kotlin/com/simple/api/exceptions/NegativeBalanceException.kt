package com.simple.api.exceptions

import java.lang.Exception
import java.math.BigDecimal

class NegativeBalanceException(
    val accountId: Long?,
    val amount: BigDecimal? = null,
    override val message: String? = null
) : Exception(message)