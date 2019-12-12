package com.simple.api.repository;

import com.simple.api.domain.Transaction;
import com.simple.api.domain.TransactionType;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class TransactionRepositoryImpl implements TransactionRepository {

    private static TransactionRepository instance = null;

    private Map<Long, Transaction> transactions = Collections.synchronizedMap(new HashMap<Long, Transaction>());
    private AtomicLong nextId = new AtomicLong(0);

    private TransactionRepositoryImpl() {}

    public static TransactionRepository getInstance() {
        if (instance == null) {
            synchronized (TransactionRepositoryImpl.class) {
                if (instance == null) {
                    instance = new TransactionRepositoryImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public Transaction create(Transaction transaction) {
        final Long id = nextId.incrementAndGet();
        transactions.put(id, new Transaction(id, transaction.getType(), transaction.getFrom(), transaction.getTo(), transaction.getAmount(), Instant.now()));
        return transactions.get(id);
    }

    @Override
    public Transaction getById(Long id) {
        return transactions.get(id);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAccountTransactions(Long accountId) {
        return transactions.values().stream().filter( t ->
            accountId.equals(t.getFrom()) || accountId.equals(t.getTo())
        ).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAccountIncomingTransactions(Long accountId) {
        return transactions.values().stream().filter( t ->
            accountId.equals(t.getTo()) && (t.getType() == TransactionType.DEPOSIT || t.getType() == TransactionType.TRANSFER)
        ).collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getAccountOutgoingTransactions(Long accountId) {
        return transactions.values().stream().filter( t ->
            accountId.equals(t.getFrom()) && (t.getType() == TransactionType.WITHDRAW || t.getType() == TransactionType.TRANSFER)
        ).collect(Collectors.toList());
    }

    @Override
    public void deleteTransactionById(Long id) {
        transactions.remove(id);
    }

    @Override
    public void deleteAllTransactions() {
        transactions.clear();
    }
}
