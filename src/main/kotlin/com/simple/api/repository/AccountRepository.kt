package com.simple.api.repository

import com.simple.api.domain.Account
import java.math.BigDecimal
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger

data class AccountRepository(
    val accounts: MutableMap<Long, Account> = Collections.synchronizedMap(HashMap<Long, Account>()),
    val nextId: AtomicInteger = AtomicInteger(0)
) {
    fun createAccount(account: Account): Account? {
        val id = nextId.incrementAndGet().toLong()
        accounts.put(id, Account(id = id, name = account.name, balance = BigDecimal.ZERO))
        return accounts.get(id)
    }

    fun updateAccount(account: Account): Account? {
        var toUpdate = accounts.get(account.id)
        toUpdate?.name = account.name
        toUpdate?.balance = account.balance
        accounts.put(toUpdate?.id!!, toUpdate)
        return accounts.get(toUpdate.id!!)
    }

    fun getAccountById(id: Long): Account? {
        return accounts.get(id)
    }
}