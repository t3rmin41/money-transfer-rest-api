package com.simple.test.api.service;

import com.simple.api.domain.Account;
import com.simple.api.domain.Transaction;
import com.simple.api.domain.TransactionType;
import com.simple.api.dto.AccountDto;
import com.simple.api.dto.TransactionDto;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.exceptions.NegativeBalanceException;
import com.simple.api.exceptions.UnknwonTransactionTypeException;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import com.simple.api.service.AccountService;
import com.simple.api.service.TransactionService;
import com.simple.test.api.tester.MultithreadedStressTester;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionServiceTest {

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    private TransactionService transactionService = new TransactionService();

    private static final int THREAD_COUNT = 10;
    private static final int INVOCATION_COUNT = 1;
    private MultithreadedStressTester stressTester = new MultithreadedStressTester(THREAD_COUNT, INVOCATION_COUNT);

    private Account john = new Account(null, "John", null);
    private Account doe = new Account(null, "Doe", null);

    private TransactionDto deposit = new TransactionDto(null, "DEPOSIT", null, 1L, BigDecimal.TEN, Instant.now());
    private TransactionDto withdrawal = new TransactionDto(null, TransactionType.WITHDRAW.name(), 1L, null, BigDecimal.ONE, Instant.now());
    private TransactionDto transfer = new TransactionDto(null, TransactionType.TRANSFER.name(), 1L, 2L, BigDecimal.ONE, Instant.now());

    @BeforeAll
    public void initData() {
        accountRepository.createAccount(john);
        accountRepository.createAccount(doe);
    }

    @Test
    @Order(1)
    public void testProcessDeposit() {
        try {
            transactionService.processTransaction(deposit);
            transactionService.getAccountIncomingTransactions("1").stream().forEach(t ->{
                assertEquals(BigDecimal.TEN, t.getAmount());
            });
        } catch (AccountNotFoundException | NegativeBalanceException | UnknwonTransactionTypeException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(2)
    public void testProcessWithdrawal() throws InterruptedException {
        try {
            transactionService.processTransaction(deposit);
            stressTester.stress(() -> {
                try {
                    transactionService.processTransaction(withdrawal);
                } catch (AccountNotFoundException | NegativeBalanceException | UnknwonTransactionTypeException e) {
                    Assertions.fail();
                }
            });
            transactionService.getAccountOutgoingTransactions("1").stream().forEach(t ->{
                assertEquals(BigDecimal.ONE, t.getAmount());
            });
        } catch (AccountNotFoundException | NegativeBalanceException | UnknwonTransactionTypeException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(3)
    public void testProcessTransfer() throws InterruptedException {
        try {
            transactionService.processTransaction(deposit);
            stressTester.stress(() -> {
                try {
                    transactionService.processTransaction(transfer);
                } catch (AccountNotFoundException | NegativeBalanceException | UnknwonTransactionTypeException e) {
                    Assertions.fail();
                }
            });
            transactionService.getAccountIncomingTransactions("1").stream().forEach(t ->{
                assertEquals(BigDecimal.TEN, t.getAmount());
            });
            transactionService.getAccountIncomingTransactions("2").stream().forEach(t ->{
                assertEquals(BigDecimal.ONE, t.getAmount());
            });
        } catch (AccountNotFoundException | NegativeBalanceException | UnknwonTransactionTypeException e) {
            Assertions.fail();
        }
    }

    @AfterAll
    public void tearDown() {
        transactionService.deleteAllTransactions();
        accountRepository.deleteAllAccounts();
    }

}
