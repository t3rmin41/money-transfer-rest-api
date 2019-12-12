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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.awaitInitialization;
import static spark.Spark.awaitStop;
import static spark.Spark.port;
import static spark.Spark.stop;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionManagementTest {

    private static final Logger log = LoggerFactory.getLogger(TransactionManagementTest.class);

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
        new TransactionController(gsonBuilder).init();
        awaitInitialization();
    }

    @BeforeEach
    public void initData() {
        accountRepository.createAccount(john);
        accountRepository.createAccount(doe);
    }

    @Test
    public void getTransactionsTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
    }

    @Test
    public void createDepositTest() {
        String endpoint = "/transactions";

        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
    }

    @Test
    public void createWithdrawalTest() {
        String endpoint = "/transactions";

        ApiTestUtils.TestResponse depositResponse = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, depositResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(depositResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse withdrawalResponse = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(withdrawal, TransactionDto.class).toString());
        assertEquals(200, withdrawalResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(withdrawalResponse.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(withdrawalResponse.body, StandardResponse.class).getData().isJsonObject());
    }

    @Test
    public void createTransferTest() {
        String endpoint = "/transactions";

        ApiTestUtils.TestResponse depositResponse = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, depositResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(depositResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse transferResponse = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(transfer, TransactionDto.class).toString());
        assertEquals(200, transferResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(transferResponse.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(transferResponse.body, StandardResponse.class).getData().isJsonObject());
    }

    @Test
    public void getAccountTransactionsTest() {
        String endpoint = "/accounts/1/transactions";
        String transactionsEndpoint = "/transactions";

        ApiTestUtils.TestResponse depositResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, depositResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(depositResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse withdrawalResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(withdrawal, TransactionDto.class).toString());
        assertEquals(200, withdrawalResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(withdrawalResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse transferResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(transfer, TransactionDto.class).toString());
        assertEquals(200, transferResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(transferResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<TransactionDto> transactions = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<TransactionDto>>(){}.getType());
        assertEquals(3, transactions.size());
    }

    @Test
    public void getAccountIncomingTransactionsTest() {
        String endpoint = "/accounts/1/transactions/incoming";
        String transactionsEndpoint = "/transactions";

        ApiTestUtils.TestResponse depositResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, depositResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(depositResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse withdrawalResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(withdrawal, TransactionDto.class).toString());
        assertEquals(200, withdrawalResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(withdrawalResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse transferResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(transfer, TransactionDto.class).toString());
        assertEquals(200, transferResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(transferResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<TransactionDto> transactions = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<TransactionDto>>(){}.getType());
        assertEquals(1, transactions.size());
    }

    @Test
    public void getAccountOutgoingTransactionsTest() {
        String endpoint = "/accounts/1/transactions/outgoing";
        String transactionsEndpoint = "/transactions";

        ApiTestUtils.TestResponse depositResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(deposit, TransactionDto.class).toString());
        assertEquals(200, depositResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(depositResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse withdrawalResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(withdrawal, TransactionDto.class).toString());
        assertEquals(200, withdrawalResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(withdrawalResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse transferResponse = ApiTestUtils.request("POST", transactionsEndpoint, gsonBuilder.create().toJsonTree(transfer, TransactionDto.class).toString());
        assertEquals(200, transferResponse.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(transferResponse.body, StandardResponse.class).getStatus());

        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<TransactionDto> transactions = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<TransactionDto>>(){}.getType());
        assertEquals(2, transactions.size());
    }

    @Test
    public void createTransferToNonExistingAccountTest() {
        String endpoint = "/transactions";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(nonExistingAccountTransfer, TransactionDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.ERROR, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
    }

    @AfterEach
    public void clearData() {
        transactionRepository.deleteAllTransactions();
        accountRepository.deleteAllAccounts();
    }

    @AfterAll
    public void tearDown() {
        transactionRepository.deleteAllTransactions();
        accountRepository.deleteAllAccounts();
        try {
            stop();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("{}", e);
        }
    }
}
