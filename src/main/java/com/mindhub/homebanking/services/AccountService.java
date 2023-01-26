package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {
    void saveAccount(Account account);

    Account getAccountById(Long id);

    List<Account> getAllAccounts();

    Account findByNumber(String number);
}