package com.simple.api.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simple.api.dto.TransactionDto;
import com.simple.api.errors.ErrorMessage;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.exceptions.NegativeBalanceException;
import com.simple.api.exceptions.UnknwonTransactionTypeException;
import com.simple.api.http.StandardResponse;
import com.simple.api.http.StatusResponse;
import com.simple.api.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransactionController implements ApiController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    private TransactionService transactionService = new TransactionService();

    private GsonBuilder gsonBuilder;

    public TransactionController(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public void init() {
        get("/transactions", this::getTransactions);
        get("/accounts/:id/transactions", this::getAccountTransactions);
        get("/accounts/:id/transactions/incoming", this::getAccountTransactions);
        get("/accounts/:id/transactions/outgoing", this::getAccountTransactions);
        post("/transactions", this::createTransaction);
    }

    public String getTransactions(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            gsonBuilder.create().toJsonTree(transactionService.getTransactions()),
                                null,
                                  null));
    }

    public String getAccountTransactions(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
        return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            gsonBuilder.create().toJsonTree(transactionService.getAccountTransactions(req.params(":id"))),
                                null,
                                  null));
    }

    public String getAccountIncomingTransactions(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
            return gsonBuilder.create().toJson(
                        new StandardResponse(StatusResponse.SUCCESS,
                                gsonBuilder.create().toJsonTree(transactionService.getAccountIncomingTransactions(req.params(":id"))),
                                     null,
                                       null));

    }

    public String getAccountOutgoingTransactions(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
        return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            gsonBuilder.create().toJsonTree(transactionService.getAccountOutgoingTransactions(req.params(":id"))),
                                 null,
                                   null));
    }

    public String createTransaction(Request req, Response res) {
        logRequest(req, res);
        res.type(ApiController.CONTENT_TYPE);
        List<ErrorMessage> errors = new ArrayList<>();
        TransactionDto toProcess = gsonBuilder.create().fromJson(req.body(), TransactionDto.class);
        try {
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.SUCCESS,
                            gsonBuilder.create().toJsonTree(transactionService.processTransaction(toProcess)),
                            null,
                            null));
        } catch (AccountNotFoundException e) {
            errors.add(new ErrorMessage("id", String.join(" : ", e.getAccountId().toString(), e.getMessage())));
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.ERROR,
                            gsonBuilder.create().toJsonTree(errors),
                            null,
                            null));
        } catch (NegativeBalanceException e) {
            errors.add(new ErrorMessage("amount", String.join(" : ", e.getAccountId().toString(), e.getAmount().toString(), e.getMessage())));
            return gsonBuilder.create().toJson(
                    new StandardResponse(StatusResponse.ERROR,
                            gsonBuilder.create().toJsonTree(errors),
                            null,
                            null));
        } catch (UnknwonTransactionTypeException e) {
            errors.add(new ErrorMessage("transactionType", String.join(" : ", e.getTransactionType(), e.getMessage())));
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
