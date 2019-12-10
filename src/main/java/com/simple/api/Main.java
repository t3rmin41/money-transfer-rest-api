package com.simple.api;

import com.google.gson.GsonBuilder;
import com.simple.api.controller.AccountController;
import com.simple.api.controller.TransactionController;
import com.simple.api.serialization.InstantDeserializer;
import com.simple.api.serialization.InstantSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static spark.Spark.port;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final int PORT = 9000;

    public static void main(String args[]) {
        log.info("Starting REST API");
        port(PORT);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantSerializer());
        gsonBuilder.registerTypeAdapter(Instant.class, new InstantDeserializer());
        gsonBuilder.setPrettyPrinting();
        new AccountController(gsonBuilder).init();
        new TransactionController(gsonBuilder).init();
    }

}
