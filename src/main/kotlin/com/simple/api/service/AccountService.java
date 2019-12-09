package com.simple.api.service;

import com.simple.api.dto.AccountDto;
import com.simple.api.repository.AccountRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class AccountService {

    @Inject
    private AccountRepository accountRepository;

    public List<AccountDto> getAccounts() {
        return accountRepository.getAccounts().stream().map(domain -> new AccountDto(domain.getId(),
                                                                                     domain.getName(),
                                                                                     domain.getBalance()))
        .collect(Collectors.toList());
    }

}
