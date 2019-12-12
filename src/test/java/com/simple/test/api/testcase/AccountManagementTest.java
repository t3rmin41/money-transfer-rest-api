package com.simple.test.api.testcase;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.simple.api.Main;
import com.simple.api.controller.AccountController;
import com.simple.api.dto.AccountDto;
import com.simple.api.http.StandardResponse;
import com.simple.api.http.StatusResponse;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import com.simple.test.api.utils.ApiTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.awaitInitialization;
import static spark.Spark.port;
import static spark.Spark.stop;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountManagementTest {

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    private GsonBuilder gsonBuilder = Main.gsonBuilder();

    private AccountDto john = new AccountDto(null, "John", null);
    private AccountDto doe = new AccountDto(1L, "Doe", null);

    @BeforeAll
    public void init() {
        port(Main.PORT);
        new AccountController(gsonBuilder).init();
        awaitInitialization();
    }

    @Test
    @Order(1)
    public void createAccountTest() {
        String endpoint = "/accounts";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("POST", endpoint, gsonBuilder.create().toJsonTree(john, AccountDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        AccountDto account = gsonBuilder.create().fromJson(dataJson, AccountDto.class);
        assertEquals(1L, account.getId().longValue());
    }

    @Test
    @Order(2)
    public void getAccountsTest() {
        String endpoint = "/accounts";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonArray());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        List<AccountDto> accounts = gsonBuilder.create().fromJson(dataJson, new TypeToken<List<AccountDto>>(){}.getType());
        assertEquals(1L, accounts.stream().findFirst().get().getId().longValue());
    }

    @Test
    @Order(3)
    public void getAccountByIdTest() {
        String endpoint = "/accounts/1";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        AccountDto account = gsonBuilder.create().fromJson(dataJson, AccountDto.class);
        assertEquals(1L, account.getId().longValue());
    }

    @Test
    @Order(4)
    public void updateAccountTest() {
        String endpoint = "/accounts/1";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("PUT", endpoint, gsonBuilder.create().toJsonTree(doe, AccountDto.class).toString());
        assertEquals(200, response.status);
        assertEquals(StatusResponse.SUCCESS, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
        assertEquals(true, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData().isJsonObject());
        JsonElement dataJson = gsonBuilder.create().fromJson(response.body, StandardResponse.class).getData();
        AccountDto account = gsonBuilder.create().fromJson(dataJson, AccountDto.class);
        assertEquals(1L, account.getId().longValue());
        assertEquals("Doe", account.getName());
    }

    @Test
    @Order(5)
    public void getNonExistingAccountByIdTest() {
        String endpoint = "/accounts/2";
        ApiTestUtils.TestResponse response = ApiTestUtils.request("GET", endpoint, null);
        assertEquals(200, response.status);
        assertEquals(StatusResponse.ERROR, gsonBuilder.create().fromJson(response.body, StandardResponse.class).getStatus());
    }

    @AfterAll
    public void tearDown() {
        accountRepository.deleteAllAccounts();
        stop();
    }

}
