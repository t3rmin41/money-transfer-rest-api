package com.simple.api.service;

import com.simple.api.domain.Account;
import com.simple.api.dto.AccountDto;
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

    public AccountDto updateAccount(AccountDto dto) {
        return accountMapper.toDto(accountRepository.updateAccount(accountMapper.toDomain(dto)));
    }

    public AccountDto getById(String id) {
        return accountMapper.toDto(accountRepository.getAccountById(new Long(id)));
    }
}
