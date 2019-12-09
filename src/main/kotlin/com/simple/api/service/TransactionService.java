package com.simple.api.service;

import com.simple.api.domain.Transaction;
import com.simple.api.repository.TransactionRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    @Inject
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactions() {
        return transactionRepository.getTransactions().stream().map(domain -> new Transaction(domain.getType(),
                                                                                              domain.getFrom(),
                                                                                              domain.getTo(),
                                                                                              domain.getAmount(),
                                                                                              domain.getTimestamp()))
         .collect(Collectors.toList());
    }

}
