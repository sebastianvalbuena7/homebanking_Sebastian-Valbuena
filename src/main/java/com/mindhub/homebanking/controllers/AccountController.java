package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAllAccounts().stream().map(account -> new AccountDTO(account)).toList();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return new AccountDTO(accountService.getAccountById(id));
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccountsClient(Authentication authentication) {
        Client client = clientService.finByEmail(authentication.getName());
        return client.getAccounts().stream().filter(account -> account.getShowAccount()).map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType) {
        Client client = clientService.finByEmail(authentication.getName());
        if(client.getAccounts().stream().filter(account -> account.getShowAccount()).collect(Collectors.toList()).size() >= 3) {
            return new ResponseEntity<>("403 Forbidden",HttpStatus.FORBIDDEN);
        }
        accountService.saveAccount(new Account("VIN" + (int) ((Math.random() * (999999 - 100000)) + 100000), LocalDateTime.now(), 0, client, accountType, true));
        return new ResponseEntity<>("201 Created", HttpStatus.CREATED);
    }

    @PostMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @PathVariable Long id) {
        Client client = clientService.finByEmail(authentication.getName());
        Set<Account> accounts = client.getAccounts();
        Account accountCurrent = accountService.getAccountById(id);
        Account accountFound = accounts.stream().filter(account -> account == accountCurrent).findFirst().orElse(null);
        if(accountCurrent.getBalance() > 0) {
            return new ResponseEntity<>("You can't delete an account with a balance", HttpStatus.FORBIDDEN);
        }

        accountService.getAccountById(accountFound.getId()).setShowAccount(false);
        List<Transaction> transactionFound = accountFound.getTransactions().stream().map(transaction -> {
            transaction.setShowTransaction(false);
            return transaction;
        }).collect(Collectors.toList());
        transactionService.saveAllTransactions(transactionFound);
        accountService.saveAccount(accountFound);

        return  new ResponseEntity<>(HttpStatus.OK);
    }
}