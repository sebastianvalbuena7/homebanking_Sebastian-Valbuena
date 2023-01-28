package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getAllLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @PostMapping("/loans/admin")
    public ResponseEntity<Object> createLoanAdmin(Authentication authentication, @RequestParam String name, @RequestParam int maxAmount, @RequestParam Integer[] payments) {
        Client client = clientService.finByEmail(authentication.getName());
        if(client.getFirstName().equals("admin")) {
            List<Integer> newList = Arrays.stream(payments).collect(Collectors.toList());
            Loan newLoan = new Loan(name, maxAmount, newList, 1.50);
            loanService.saveLoan(newLoan);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {
        Client client = clientService.finByEmail(authentication.getName());
        Loan typeLoan = loanService.getLoanById(loanApplicationDTO.getId());
        Account accountDestiny = accountService.findByNumber(loanApplicationDTO.getNumberDestiny());
        Set<Account> accounts = client.getAccounts().stream().filter(account -> account.getNumber().equals(loanApplicationDTO.getNumberDestiny())).collect(Collectors.toSet());

        if(loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayment() == 0 || loanApplicationDTO.getNumberDestiny().equals("")) {
            return new ResponseEntity<>("Data incomplete", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() > typeLoan.getMaxAmount()) {
            return new ResponseEntity<>("The requested amount exceeds the maximum loan amount", HttpStatus.FORBIDDEN);
        }
        if(!typeLoan.getPayments().contains(loanApplicationDTO.getPayment())) {
            return new ResponseEntity<>("The number of installments is not among those available for the loan",HttpStatus.FORBIDDEN);
        }
        if(accounts.size() == 0) {
            return new ResponseEntity<>("The account does not belong to you", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() < 100000) {
            return new ResponseEntity<>("The amount must be greater than $100.000", HttpStatus.FORBIDDEN);
        }
        if(client.getClientLoan().stream().filter(clientLoan -> clientLoan.getLoan().getId() == typeLoan.getId()).count() == 1) {
           return new ResponseEntity<>("You can't have two loans of the same type", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() * typeLoan.getLoanPercentage(), loanApplicationDTO.getPayment(), LocalDateTime.now(), client, typeLoan);
        clientLoanService.saveClientLoan(clientLoan);
        Transaction transaction = new Transaction(CREDIT, loanApplicationDTO.getAmount(), typeLoan.getName() + " loan approved", LocalDateTime.now(), accountDestiny, true);
        transaction.setBalanceCurrent(accountDestiny.getBalance() + loanApplicationDTO.getAmount());
        transactionService.saveTransaction(transaction);
        accountDestiny.setBalance(accountDestiny.getBalance() + loanApplicationDTO.getAmount());
        accountService.saveAccount(accountDestiny);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}