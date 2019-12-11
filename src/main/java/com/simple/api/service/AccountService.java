package com.simple.api.service;

import com.simple.api.domain.Account;
import com.simple.api.dto.AccountDto;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.mapper.AccountMapperKt;
import com.simple.api.repository.AccountRepository;
import com.simple.api.repository.AccountRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

public class AccountService {

    private AccountRepository accountRepository = AccountRepositoryImpl.getInstance();

    public List<AccountDto> getAccounts() {
        return accountRepository.getAccounts().stream().map(domain ->
            new AccountDto(domain.getId(), domain.getName(), domain.getBalance()))
        .collect(Collectors.toList());
    }

    public AccountDto createAccount(AccountDto dto) {
        return AccountMapperKt.toDto(accountRepository.createAccount(new Account(null, dto.getName(), null)));
    }

    public AccountDto updateAccount(String id, AccountDto dto) throws AccountNotFoundException {
        return AccountMapperKt.toDto(accountRepository.updateAccountName(new Long(id), AccountMapperKt.toDomain(dto)));
    }

    public AccountDto getById(String id) throws AccountNotFoundException {
        try {
            return AccountMapperKt.toDto(accountRepository.getAccountById(new Long(id)));
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException(e.getAccountId(), e.getMessage());
        }
    }
}
