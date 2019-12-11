package com.simple.test.api.testcase;

import com.simple.api.domain.Account;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import com.simple.test.api.tester.MultithreadedStressTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountStressTester {

    private static final int THREAD_COUNT = 100;
    private static final int INVOCATION_COUNT = 1;

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    private MultithreadedStressTester stressTester = new MultithreadedStressTester(THREAD_COUNT, INVOCATION_COUNT);

    private Account alice = new Account(null, "Alice", null);

    @Test
    public void testAccountRepositoryAccountCreation() throws InterruptedException {
        stressTester.stress(new Runnable() {
            @Override
            public void run() {
                accountRepository.createAccount(alice);
            }
        });
        stressTester.shutdown();
    }

    @AfterAll
    public void testCount() {
        assertEquals(THREAD_COUNT, accountRepository.getAccounts().stream().count());
    }

}
