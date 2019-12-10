package com.simple.api.repository;

import com.simple.api.domain.Transaction;

import java.util.List;

public interface TransactionRepository {

    Transaction create(Transaction transaction);
    List<Transaction> getTransactions();
    List<Transaction> getAccountTransactions(Long accountId);
    List<Transaction> getAccountIncomingTransactions(Long accountId);
    List<Transaction> getAccountOutgoingTransactions(Long accountId);
}
