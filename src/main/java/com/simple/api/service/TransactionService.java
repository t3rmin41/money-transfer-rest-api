package com.simple.api.service;

import com.simple.api.dto.TransactionDto;
import com.simple.api.repository.TransactionRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    private TransactionRepository transactionRepository = new TransactionRepository();

    public List<TransactionDto> getTransactions() {
        return transactionRepository.getTransactions().values().stream().map(domain ->
            new TransactionDto(domain.getType(), domain.getFrom(), domain.getTo(), domain.getAmount(), domain.getTimestamp()))
         .collect(Collectors.toList());
    }

}
