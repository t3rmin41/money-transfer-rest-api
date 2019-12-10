package com.simple.api.repository;

import com.simple.api.domain.Account;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.exceptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository {

    String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";
    String NEGATIVE_BALANCE_MESSAGE = "Cannot decrease the amount";

    List<Account> getAccounts();
    Account createAccount(Account account);
    Account updateAccountName(Long id, Account account) throws AccountNotFoundException;
    Account getAccountById(Long id) throws AccountNotFoundException;
    Account increaseAccountBalance(Long id, BigDecimal amount) throws AccountNotFoundException;
    Account decreaseAccountBalance(Long id, BigDecimal amount) throws AccountNotFoundException, NegativeBalanceException;
    void transferBetweenAccounts(Long fromId, Long toId, BigDecimal amount) throws AccountNotFoundException, NegativeBalanceException;
}
