package com.simple.test.api.testcase;

import com.simple.api.domain.Transaction;
import com.simple.api.domain.TransactionType;
import com.simple.api.repository.TransactionRepository;
import com.simple.api.repository.TransactionRepositoryImpl;
import com.simple.test.api.tester.MultithreadedStressTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionStressTest {

    private static final int THREAD_COUNT = 100;
    private static final int INVOCATION_COUNT = 1;

    private TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    private MultithreadedStressTester stressTester = new MultithreadedStressTester(THREAD_COUNT, INVOCATION_COUNT);

    private Transaction transaction = new Transaction(null, TransactionType.DEPOSIT, null, 1L, BigDecimal.ONE, Instant.now());

    @Test
    public void testAccountRepositoryMultithreadedCreation() throws InterruptedException {
        stressTester.stress(() -> {
            transactionRepository.create(transaction);
        });
        stressTester.shutdown();
    }

    @AfterAll
    public void testCount() {
        assertEquals(THREAD_COUNT, transactionRepository.getTransactions().stream().count());
        long index = 1;
        for (Transaction t : transactionRepository.getTransactions()) {
            assertEquals(index, transactionRepository.getById(index).getId().longValue());
            index++;
        }
    }

}
