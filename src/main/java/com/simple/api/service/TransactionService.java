package com.simple.api.service;

import com.simple.api.dto.TransactionDto;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.exceptions.NegativeBalanceException;
import com.simple.api.exceptions.UnknwonTransactionTypeException;
import com.simple.api.mapper.TransactionMapperKt;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;
import com.simple.api.repository.TransactionRepository;
import com.simple.api.repository.TransactionRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {

    private TransactionRepository transactionRepository = TransactionRepositoryImpl.getInstance();
    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    public List<TransactionDto> getTransactions() {
        return transactionRepository.getTransactions().stream().map(domain ->
            new TransactionDto(domain.getId(), domain.getType().name(), domain.getFrom(), domain.getTo(), domain.getAmount(), domain.getTimestamp()))
         .collect(Collectors.toList());
    }

    public List<TransactionDto> getAccountTransactions(String accountId) {
        return transactionRepository.getAccountTransactions(new Long(accountId)).stream().map(domain -> TransactionMapperKt.toDto(domain))
               .collect(Collectors.toList());
    }

    public List<TransactionDto> getAccountIncomingTransactions(String accountId) {
        return transactionRepository.getAccountIncomingTransactions(new Long(accountId)).stream().map(domain -> TransactionMapperKt.toDto(domain))
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getAccountOutgoingTransactions(String accountId) {
        return transactionRepository.getAccountOutgoingTransactions(new Long(accountId)).stream().map(domain -> TransactionMapperKt.toDto(domain))
                .collect(Collectors.toList());
    }

    public TransactionDto processTransaction(TransactionDto dto) throws AccountNotFoundException, NegativeBalanceException, UnknwonTransactionTypeException {
        switch (dto.getType()) {
            case "DEPOSIT":  return processDeposit(dto);
            case "WITHDRAW": return processWithdrawal(dto);
            case "TRANSFER": return processTransfer(dto);
            default :      throw new UnknwonTransactionTypeException("UNKNOWN", "Transaction type unknown");
        }
    }

    private TransactionDto processDeposit(TransactionDto dto) throws AccountNotFoundException {
        try {
            accountRepository.increaseAccountBalance(dto.getTo(), dto.getAmount());
            return TransactionMapperKt.toDto(transactionRepository.create(TransactionMapperKt.toDomain(dto)));
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException(e.getAccountId(), e.getMessage());
        }
    }

    private TransactionDto processWithdrawal(TransactionDto dto) throws AccountNotFoundException, NegativeBalanceException {
        try {
            accountRepository.decreaseAccountBalance(dto.getFrom(), dto.getAmount());
            return TransactionMapperKt.toDto(transactionRepository.create(TransactionMapperKt.toDomain(dto)));
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException(e.getAccountId(), e.getMessage());
        } catch (NegativeBalanceException e) {
            throw new NegativeBalanceException(e.getAccountId(), e.getAmount(), e.getMessage());
        }
    }

    private TransactionDto processTransfer(TransactionDto dto) throws AccountNotFoundException, NegativeBalanceException {
        try {
            accountRepository.transferBetweenAccounts(dto.getFrom(), dto.getTo(), dto.getAmount());
            return TransactionMapperKt.toDto(transactionRepository.create(TransactionMapperKt.toDomain(dto)));
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException(e.getAccountId(), e.getMessage());
        } catch (NegativeBalanceException e) {
            throw new NegativeBalanceException(e.getAccountId(), e.getAmount(), e.getMessage());
        }
    }

    public void deleteTransactionById(String id) {
        transactionRepository.deleteTransactionById(new Long(id));
    }

    public void deleteAllTransactions() {
        transactionRepository.deleteAllTransactions();
    }
}
