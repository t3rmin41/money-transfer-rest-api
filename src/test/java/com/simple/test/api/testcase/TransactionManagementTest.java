package com.simple.test.api.testcase;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.simple.api.Main;
import com.simple.api.controller.TransactionController;
import com.simple.api.domain.Account;
import com.simple.api.domain.TransactionType;
import com.simple.api.dto.TransactionDto;
import com.simple.api.http.StandardResponse;
import com.simple.api.http.StatusResponse;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import com.simple.api.repository.TransactionRepository;
import com.simple.api.repository.TransactionRepositoryImpl;
import com.simple.test.api.utils.ApiTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.awaitInitialization;
import static spark.Spark.port;
import static spark.Spark.stop;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionManagementTest {

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();
    private TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    private GsonBuilder gsonBuilder = Main.gsonBuilder();

    private Account john = new Account(null, "John", null);
    private Account doe = new Account(null, "Doe", null);

    private TransactionDto deposit = new TransactionDto(null, "DEPOSIT", null, 1L, BigDecimal.TEN, Instant.now());
    private TransactionDto withdrawal = new TransactionDto(null, TransactionType.WITHDRAW.name(), 1L, null, BigDecimal.ONE, Instant.now());
    private TransactionDto transfer = new TransactionDto(null, TransactionType.TRANSFER.name(), 1L, 2L, BigDecimal.ONE, Instant.now());
    private TransactionDto nonExistingAccountTransfer = new TransactionDto(null, TransactionType.TRANSFER.name(), 2L, 3L, BigDecimal.ONE, Instant.now());

    @BeforeAll
    public void init() {
        port(Main.PORT);
        new TransactionController(Main.gsonBuilder()).init();
        awaitInitialization();
        accountRepository.createAccount(john);
        accountRepository.createAccount(doe);
    }

    @Test
    @Order(1)
    public void getTransactionsTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
    }

    @Test
    @Order(1)
    public void createDepositTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
    }

    @Test
    @Order(2)
    public void createWithdrawalTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(withdrawal, TransactionDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
    }

    @Test
    @Order(3)
    public void createTransferTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(transfer, TransactionDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
    }

    @Test
    @Order(4)
    public void getAccountTransactionsTest() {
        String endpoint = "/accounts/1/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<TransactionDto> transactions = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<TransactionDto>>(){}.getType());
        assertEquals(3, transactions.size());
    }

    @Test
    @Order(5)
    public void getAccountIncomingTransactionsTest() {
        String endpoint = "/accounts/1/transactions/incoming";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<TransactionDto> transactions = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<TransactionDto>>(){}.getType());
        assertEquals(1, transactions.size());
    }

    @Test
    @Order(5)
    public void getAccountOutgoingTransactionsTest() {
        String endpoint = "/accounts/1/transactions/outgoing";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<TransactionDto> transactions = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<TransactionDto>>(){}.getType());
        assertEquals(2, transactions.size());
    }

    @Test
    @Order(6)
    public void createTransferToNonExistingAccountTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(nonExistingAccountTransfer, TransactionDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.ERROR, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
    }

    @AfterAll
    public void tearDown() {
        accountRepository.deleteAllAccounts();
        transactionRepository.deleteAllTransactions();
        stop();
    }
}
