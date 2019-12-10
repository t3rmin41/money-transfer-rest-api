package com.simple.api.mapper

import com.simple.api.domain.Account
import com.simple.api.dto.AccountDto

class AccountMapper {
    fun Account.toDto(): AccountDto =
            AccountDto(
                    id = this.id!!,
                    name = this.name,
                    balance = this.balance
            )

    fun AccountDto.toDomain(): Account =
            Account(
                    id = this.id,
                    name = this.name,
                    balance = this.balance
            )
}

