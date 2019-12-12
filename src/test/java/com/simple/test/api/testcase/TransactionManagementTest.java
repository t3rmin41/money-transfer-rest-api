package com.simple.test.api.testcase;

import com.simple.api.Main;
import com.simple.api.controller.AccountController;
import com.simple.api.controller.TransactionController;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import com.simple.api.repository.TransactionRepository;
import com.simple.api.repository.TransactionRepositoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static spark.Spark.awaitInitialization;
import static spark.Spark.port;
import static spark.Spark.stop;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionManagementTest {

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();
    private TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();

    @BeforeAll
    public void init() {
        port(Main.PORT);
        new TransactionController(Main.gsonBuilder()).init();
        awaitInitialization();
    }


    @AfterAll
    public void tearDown() {
        accountRepository.deleteAllAccounts();
        transactionRepository.deleteAllTransactions();
        stop();
    }
}
