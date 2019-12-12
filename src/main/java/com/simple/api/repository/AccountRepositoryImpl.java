package com.simple.api.repository;

import com.simple.api.domain.Account;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.exceptions.NegativeBalanceException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class AccountRepositoryImpl implements AccountRepository {

    private static AccountRepository instance = null;

    private Map<Long, Account> accounts = Collections.synchronizedMap(new HashMap<>());
    private AtomicLong nextId = new AtomicLong(0);

    private AccountRepositoryImpl() {}

    public static AccountRepository getInstance() {
        if (instance == null) {
            synchronized (AccountRepositoryImpl.class) {
                if (instance == null) {
                    instance = new AccountRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Account> getAccounts() {
        return accounts.values().stream().collect(Collectors.toList());
    }

    @Override
    public Account createAccount(Account account) {
        final Long id = nextId.incrementAndGet();
        accounts.put(id, new Account(id, account.getName(), BigDecimal.ZERO));
        return accounts.get(id);
    }

    @Override
    public Account updateAccountName(Long id, Account account) throws AccountNotFoundException {
        try {
            final Account toUpdate = accounts.get(id);
            toUpdate.setName(account.getName() != null ? account.getName() : toUpdate.getName());
            accounts.put(toUpdate.getId(), toUpdate);
            return accounts.get(toUpdate.getId());
        } catch (NullPointerException e) {
            throw new AccountNotFoundException(id, ACCOUNT_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public Account getAccountById(Long id) throws AccountNotFoundException {
        Account account = accounts.get(id);
        if (null != account) {
            return account;
        } else {
            throw new AccountNotFoundException(id, ACCOUNT_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public synchronized Account increaseAccountBalance(Long id, BigDecimal amount) throws AccountNotFoundException {
        Account account = accounts.get(id);
        if (null != account) {
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            return accounts.get(id);
        } else {
            throw new AccountNotFoundException(id, ACCOUNT_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public synchronized Account decreaseAccountBalance(Long id, BigDecimal amount) throws AccountNotFoundException, NegativeBalanceException {
        Account account = accounts.get(id);
        if (null != account) {
            if (account.getBalance().compareTo(amount) == -1) {
                throw new NegativeBalanceException(account.getId(), amount, NEGATIVE_BALANCE_MESSAGE);
            }
            BigDecimal newBalance = account.getBalance().subtract(amount);
            account.setBalance(newBalance);
            return accounts.get(id);
        } else {
            throw new AccountNotFoundException(id, ACCOUNT_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    public void transferBetweenAccounts(Long fromId, Long toId, BigDecimal amount) throws AccountNotFoundException, NegativeBalanceException {
        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);
        decreaseAccountBalance(from.getId(), amount); // decrease first
        increaseAccountBalance(to.getId(), amount);
    }

    @Override
    public void deleteAccountById(Long id) {
        accounts.remove(id);
    }

    @Override
    public void deleteAllAccounts() {
        accounts.clear();
    }
}
