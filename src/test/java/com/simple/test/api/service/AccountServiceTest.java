package com.simple.test.api.service;

import com.simple.api.dto.AccountDto;
import com.simple.api.service.AccountService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

public class AccountServiceTest {

    private AccountService accountService = new AccountService();

    private AccountDto bob = new AccountDto(1L, "Bob", BigDecimal.ZERO);
    private AccountDto alice = new AccountDto(2L, "Alice", BigDecimal.ZERO);
    private AccountDto anonymous = new AccountDto(3L, null, BigDecimal.ZERO);

    @BeforeAll
    public void initData() {
        accountService.getAccounts().addAll(Arrays.asList(bob, alice, anonymous));
    }

    @Test
    public void testCreateAccount() {

    }


    @AfterAll
    public void tearDown() {
        accountService.getAccounts().clear();
    }
}
