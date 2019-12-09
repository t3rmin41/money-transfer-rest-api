package com.simple.api.repository

import com.simple.api.domain.Account
import java.util.Collections

data class AccountRepository(
    val accounts: Collection<Account> = Collections.synchronizedCollection(ArrayList<Account>())
)