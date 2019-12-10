package com.simple.api.controller;

import com.google.gson.Gson;
import com.simple.api.dto.AccountDto;
import com.simple.api.http.StandardResponse;
import com.simple.api.http.StatusResponse;
import com.simple.api.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class AccountController implements ApiController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService = new AccountService();

    public void init() {
        get("/users", this::getAccounts);
        get("/users/:id", this::getAccountById);
        post("/users", this::createAccount);
        put("/users/:id", this::updateAccount);
    }

    public String getAccounts(Request req, Response res) {
        log.info("{}", req.pathInfo());
        res.type(ApiController.CONTENT_TYPE);
        return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(accountService.getAccounts()), null));
    }

    public String getAccountById(Request req, Response res) {
        log.info("{}", req.pathInfo());
        res.type(ApiController.CONTENT_TYPE);
        return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(accountService.getById(req.params(":id"))), null));
    }

    public String createAccount(Request req, Response res) {
        log.info("{}", req.pathInfo());
        res.type(ApiController.CONTENT_TYPE);
        AccountDto dto = new Gson().fromJson(req.body(), AccountDto.class);
        return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(accountService.createAccount(dto)), null));
    }

    public String updateAccount(Request req, Response res) {
        log.info("{}", req.pathInfo());
        res.type(ApiController.CONTENT_TYPE);
        AccountDto dto = new Gson().fromJson(req.body(), AccountDto.class);
        return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(accountService.updateAccount(dto)), null));
    }

}
