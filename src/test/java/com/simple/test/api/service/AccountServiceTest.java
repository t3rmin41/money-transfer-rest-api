package com.simple.test.api.service;

import com.simple.api.dto.AccountDto;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.service.AccountService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest {

    private AccountService accountService = new AccountService();

    private AccountDto john = new AccountDto(null, "John", null);
    private AccountDto doe = new AccountDto(null, "Doe", null);
    private AccountDto anonymous = new AccountDto(null, null, null);

    @BeforeAll
    public void initData() {
        accountService.createAccount(john);
        accountService.createAccount(doe);
        accountService.createAccount(anonymous);
    }

    @AfterAll
    public void tearDown() {
        accountService.deleteAllAccounts();
    }

    @Test
    @Order(1)
    public void testCreateAccount() {
        accountService.createAccount(new AccountDto(null, "Alice", null));
        try {
            assertEquals(4, accountService.getById("4").getId().longValue());
            assertEquals("Alice", accountService.getById("4").getName());
            assertEquals(BigDecimal.ZERO, accountService.getById("4").getBalance());
        } catch (AccountNotFoundException e) {
            Assertions.fail();
        }
    }

    @Test
    @Order(2)
    public void findByIdTest() {
        try {
            assertEquals(2L, accountService.getById("2").getId().longValue());
        } catch (AccountNotFoundException e) {
            Assertions.fail();
        }
    }

}
