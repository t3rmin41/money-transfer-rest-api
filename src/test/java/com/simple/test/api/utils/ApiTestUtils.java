package com.simple.test.api.utils;

import com.google.gson.Gson;
import com.simple.api.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.utils.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class ApiTestUtils {

    private static final List<String> METHODS_WITH_BODY = Arrays.asList("POST", "PUT", "PATCH");

    private static final Logger log = LoggerFactory.getLogger(ApiTestUtils.class);

    private static final String HOST = "http://localhost";

    public static TestResponse request(String method, String path, String requestBody) {
        DataOutputStream writer = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(HOST + ":" + Main.PORT + path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            if (METHODS_WITH_BODY.contains(method)) {
                connection.setDoInput(true);
                writer = new DataOutputStream(connection.getOutputStream());
                writer.writeBytes(requestBody);
                writer.flush();
            }
            connection.connect();
            String body = IOUtils.toString(connection.getInputStream());
            return new TestResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            log.error("{}", e);
            fail("Sending request failed: " + e.getMessage());
            return null;
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.error("{}", e);
            } catch (NullPointerException e) {
                if (!METHODS_WITH_BODY.contains(method)) {
                    //ignore
                }
            }
        }
    }

    public static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public Map<String,String> json() {
            return new Gson().fromJson(body, HashMap.class);
        }
    }
}
