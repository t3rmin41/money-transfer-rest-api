package com.simple.api.repository

import com.simple.api.domain.Transaction
import java.util.Collections

data class TransactionRepository(
    val transactions: Map<String, Transaction> = Collections.synchronizedMap(HashMap<String, Transaction>())
)