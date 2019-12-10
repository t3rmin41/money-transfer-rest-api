package com.simple.api.repository

import com.simple.api.domain.Account
import com.simple.api.exceptions.AccountNotFoundException
import java.math.BigDecimal
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger

data class AccountRepository(
    val accounts: MutableMap<Long, Account> = Collections.synchronizedMap(HashMap<Long, Account>()),
    val nextId: AtomicInteger = AtomicInteger(0)
) {
    private companion object {
        private const val ACCOUNT_NOT_FOUND_MESSAGE = "Account not found"
    }

    fun createAccount(account: Account): Account? {
        val id = nextId.incrementAndGet().toLong()
        accounts.put(id, Account(id = id, name = account.name, balance = BigDecimal.ZERO))
        return accounts.get(id)
    }

    @Throws(AccountNotFoundException::class)
    fun updateAccount(id: Long, account: Account): Account? {
        try {
            var toUpdate = accounts.get(id)
            account.name?.let { toUpdate?.name = account.name }
            account.balance?.let { toUpdate?.balance = account.balance }
            accounts.put(toUpdate?.id!!, toUpdate)
            return accounts.get(toUpdate.id!!)
        } catch (e: NullPointerException) {
            throw AccountNotFoundException(id, ACCOUNT_NOT_FOUND_MESSAGE)
        }
    }

    @Throws(AccountNotFoundException::class)
    fun getAccountById(id: Long): Account? {
        return accounts.get(id) ?: throw AccountNotFoundException(id, ACCOUNT_NOT_FOUND_MESSAGE)
    }
}