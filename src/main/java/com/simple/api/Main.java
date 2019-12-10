package com.simple.api;

import com.simple.api.controller.AccountController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final int PORT = 8000;

    public static void main(String args[]) {
        log.info("Starting REST API");
        port(PORT);
        new AccountController().init();
    }

}
