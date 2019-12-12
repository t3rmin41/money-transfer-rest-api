package com.simple.test.api.repository;

import com.simple.api.domain.Account;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.exceptions.NegativeBalanceException;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountRepositoryTest {

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    private Account john = new Account(null, "John", null);
    private Account doe = new Account(null, "Doe", null);
    private Account anonymous = new Account(null, null, null);

    @BeforeAll
    public void initData() {
        accountRepository.createAccount(john);
        accountRepository.createAccount(doe);
        accountRepository.createAccount(anonymous);
    }

    @AfterAll
    public void tearDown() {
        accountRepository.deleteAllAccounts();
    }

    @Test
    public void testCreateAccount() {
        accountRepository.createAccount(new Account(null, "Alice", null));
        try {
            assertEquals(4, accountRepository.getAccountById(4L).getId().longValue());
            assertEquals("Alice", accountRepository.getAccountById(4L).getName());
            assertEquals(BigDecimal.ZERO, accountRepository.getAccountById(4L).getBalance());
        } catch (AccountNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    public void findByIdTest() {
        try {
            assertEquals(2L, accountRepository.getAccountById(2L).getId().longValue());
        } catch (AccountNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    public void increaseBalanceTest() {
        try {
            accountRepository.increaseAccountBalance(1L, BigDecimal.TEN);
            assertEquals(BigDecimal.TEN, accountRepository.getAccountById(1L).getBalance());
        } catch (AccountNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    public void decreaseBalanceTest() {
        try {
            accountRepository.getAccountById(1L).setBalance(BigDecimal.ZERO);
            accountRepository.increaseAccountBalance(1L, BigDecimal.TEN);
            accountRepository.decreaseAccountBalance(1L, BigDecimal.TEN);
            assertEquals(BigDecimal.ZERO, accountRepository.getAccountById(1L).getBalance());
        } catch (AccountNotFoundException | NegativeBalanceException e) {
            Assertions.fail();
        }
        try {
            accountRepository.decreaseAccountBalance(1L, BigDecimal.valueOf(0.01));
        } catch (NegativeBalanceException e) {
            assertEquals(true, true);
        } catch (AccountNotFoundException e) {
            Assertions.fail();
        }
    }
}
