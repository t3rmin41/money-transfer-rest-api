package com.simple.api.service;

import com.simple.api.domain.Account;
import com.simple.api.dto.AccountDto;
import com.simple.api.exceptions.AccountNotFoundException;
import com.simple.api.mapper.AccountMapper;
import com.simple.api.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AccountService {

    private AccountRepository accountRepository = new AccountRepository();

    private AccountMapper accountMapper = new AccountMapper();

    public List<AccountDto> getAccounts() {
        return accountRepository.getAccounts().values().stream().map(domain ->
            new AccountDto(domain.getId(), domain.getName(), domain.getBalance()))
        .collect(Collectors.toList());
    }

    public AccountDto createAccount(AccountDto dto) {
        return accountMapper.toDto(accountRepository.createAccount(new Account(null, dto.getName(), null)));
    }

    public AccountDto updateAccount(String id, AccountDto dto) throws AccountNotFoundException {
        getById(id);
        return accountMapper.toDto(accountRepository.updateAccount(new Long(id), accountMapper.toDomain(dto)));
    }

    public AccountDto getById(String id) throws AccountNotFoundException {
        try {
            return accountMapper.toDto(accountRepository.getAccountById(new Long(id)));
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException(e.getAccountId(), e.getMessage());
        }
    }
}
