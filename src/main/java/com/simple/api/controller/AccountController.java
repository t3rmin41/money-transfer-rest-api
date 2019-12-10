package com.simple.api.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simple.api.dto.AccountDto;
import com.simple.api.errors.ErrorMessage;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.http.StandardResponse;
import com.simple.api.http.StatusResponse;
import com.simple.api.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class AccountController implements ApiController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private GsonBuilder gsonBuilder;

    private AccountService accountService = new AccountService();

    public AccountController(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public void init() {
        get("/accounts", this::getAccounts);
        get("/accounts/:id", this::getAccountById);
        post("/accounts", this::createAccount);
        put("/accounts/:id", this::updateAccount);
    }

    public String getAccounts(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        return gsonBuilder.create().toJson(
                new StandardResponse(StatusResponse.SUCCESS,
                        gsonBuilder.create().toJsonTree(accountService.getAccounts()),
                            null,
                              null));
    }

    public String getAccountById(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
        try {
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            gsonBuilder.create().toJsonTree(accountService.getById(req.params(":id"))),
                                 null,
                                   null));
        } catch (AccountNotFoundException e) {
            errors.add(new ErrorMessage("id", String.join(" : ", e.getAccountId().toString(), e.getMessage())));
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.ERROR,
                            gsonBuilder.create().toJsonTree(errors),
                                 null,
                                   null));
        }
    }

    public String createAccount(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
        AccountDto dto = gsonBuilder.create().fromJson(req.body(), AccountDto.class);
        return gsonBuilder.create().toJson(
                new StandardResponse(StatusResponse.SUCCESS,
                        gsonBuilder.create().toJsonTree(accountService.createAccount(dto)),
                            null,
                              null));
    }

    public String updateAccount(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
        AccountDto fieldsToEdit = gsonBuilder.create().fromJson(req.body(), AccountDto.class);
        try {
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            gsonBuilder.create().toJsonTree(accountService.updateAccount(req.params(":id"), fieldsToEdit)),
                                 null,
                                   null));
        } catch (AccountNotFoundException e) {
            errors.add(new ErrorMessage("id", String.join(" : ", e.getAccountId().toString(), e.getMessage())));
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.ERROR,
                            gsonBuilder.create().toJsonTree(errors),
                                null,
                                  null));
        }
    }

    private void logRequest(Request req, Response res) {
        log.info("{} {}", req.requestMethod(), req.pathInfo());
    }
}
