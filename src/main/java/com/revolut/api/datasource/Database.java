package com.revolut.api.datasource;

import com.revolut.api.domain.Account;
import com.revolut.api.domain.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Database {

    private Collection<Account> accounts = Collections.synchronizedCollection(new ArrayList<>());

    private Collection<Transaction> transactions = Collections.synchronizedCollection(new ArrayList<>());

    public Collection<Account> getAccounts() {
        return this.accounts;
    }

    public Collection<Transaction> getTransactions() {
        return this.transactions;
    }

}
