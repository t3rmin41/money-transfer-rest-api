package com.simple.api.exceptions

import java.lang.Exception

class UnknwonTransactionTypeException (
    val transactionType: String? = null,
    override val message: String? = null
) : Exception(message) {}