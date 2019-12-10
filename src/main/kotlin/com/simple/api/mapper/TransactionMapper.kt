package com.simple.api.mapper

import com.simple.api.domain.Transaction
import com.simple.api.dto.TransactionDto

class TransactionMapper {

    fun Transaction.toDto(): TransactionDto =
        TransactionDto(
            id = this.id,
            type = this.type,
            from = this.from,
            to = this.to,
            amount = this.amount,
            timestamp = this.timestamp
        )

    fun TransactionDto.toDomain(): Transaction =
        Transaction(
            id = this.id,
            type = this.type,
            from = this.from,
            to = this.to,
            amount = this.amount,
            timestamp = this.timestamp
        )
}