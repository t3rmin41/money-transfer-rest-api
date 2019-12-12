package com.simple.test.api.repository;

import com.simple.api.domain.Transaction;
import com.simple.api.domain.TransactionType;
import com.simple.api.repository.TransactionRepository;
import com.simple.api.repository.TransactionRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionRepositoryTest {

    private TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    private Transaction deposit = new Transaction(null, TransactionType.DEPOSIT, null, 1L, BigDecimal.TEN, Instant.now());
    private Transaction withdrawal = new Transaction(null, TransactionType.WITHDRAW, 1L, null, BigDecimal.ONE, Instant.now());
    private Transaction transfer = new Transaction(null, TransactionType.TRANSFER, 1L, 2L, BigDecimal.ONE, Instant.now());


    @BeforeEach
    public void initData() {
        transactionRepository.create(deposit);
        transactionRepository.create(withdrawal);
        transactionRepository.create(transfer);
    }

    @Test
    public void createTransactionTest() {
        transactionRepository.create(new Transaction(null, TransactionType.TRANSFER, 1L, 2L, BigDecimal.valueOf(0.5), Instant.now()));
        assertEquals(BigDecimal.valueOf(0.5), transactionRepository.getById(4L).getAmount());
    }

    @Test
    public void getTransactionsTest() {
        long index = 1;
        for (Transaction t : transactionRepository.getTransactions()) {
            assertEquals(index, t.getId().longValue());
            index++;
        }
    }

    @Test
    public void getAccountTransactionsTest() {
        assertEquals(3, transactionRepository.getAccountTransactions(1L).size());
        assertEquals(1, transactionRepository.getAccountTransactions(2L).size());
    }

    @Test
    public void getAccountIncomingTransactionsTest() {
        assertEquals(1, transactionRepository.getAccountIncomingTransactions(1L).size());
        assertEquals(1, transactionRepository.getAccountIncomingTransactions(2L).size());
    }

    @Test
    public void getAccountOutgoingTransactions() {
        assertEquals(2, transactionRepository.getAccountOutgoingTransactions(1L).size());
        assertEquals(0, transactionRepository.getAccountOutgoingTransactions(2L).size());
    }

    @AfterEach
    public void tearDown() {
        transactionRepository.deleteAllTransactions();
    }

}
