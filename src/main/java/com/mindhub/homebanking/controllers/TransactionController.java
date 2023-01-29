package com.mindhub.homebanking.controllers;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.PDFTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.CreatePDF;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transaction(Authentication authentication, @RequestParam Double amount, @RequestParam String description, @RequestParam String numberOrigin, @RequestParam String numberDestiny
    ) {
        Client client = clientService.finByEmail(authentication.getName());
        Account accountOrigin = accountService.findByNumber(numberOrigin);
        Account accountDestiny = accountService.findByNumber(numberDestiny);
        Set<Account> accounts = client.getAccounts().stream().filter(account -> account.getNumber().equals(numberOrigin)).collect(Collectors.toSet());

        if(amount.isNaN() || description.isEmpty() || numberOrigin.isEmpty() || numberDestiny.isEmpty()) {
            return new ResponseEntity<>("Datos incompletos", HttpStatus.FORBIDDEN);
        }
        if(numberOrigin.equals(numberDestiny)) {
            return new ResponseEntity<>("Cuentas iguales", HttpStatus.FORBIDDEN);
        }
        if(accountOrigin == null || accountDestiny == null) {
            return new ResponseEntity<>("La cuenta no existe", HttpStatus.FORBIDDEN);
        }
        if(accounts.size() == 0) {
            return new ResponseEntity<>("El numero de cuenta no existe", HttpStatus.FORBIDDEN);
        }
        if(accountOrigin.getBalance() < amount) {
            return new ResponseEntity<>("La cuenta no tiene monto suficiente", HttpStatus.FORBIDDEN);
        }
        if(amount.isNaN() || amount < 0) {
            return new ResponseEntity<>("No i cant", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(DEBIT, amount, description + " " + accountDestiny.getNumber(), LocalDateTime.now(), accountOrigin, true);
        transactionDebit.setBalanceCurrent(accountOrigin.getBalance() - amount);
        transactionService.saveTransaction(transactionDebit);

        Transaction transactionCredit = new Transaction(CREDIT, amount, description + " " + accountOrigin.getNumber(), LocalDateTime.now(), accountDestiny, true);
        transactionCredit.setBalanceCurrent(accountDestiny.getBalance() + amount);
        transactionService.saveTransaction(transactionCredit);

        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountService.saveAccount(accountOrigin);

        accountDestiny.setBalance(accountDestiny.getBalance() + amount);
        accountService.saveAccount(accountDestiny);

        return new ResponseEntity<>("201 CREATED", HttpStatus.CREATED);
    }

    @PostMapping("/export-pdf")
    public ResponseEntity<Object> exportPDF(HttpServletResponse response, Authentication authentication, @RequestBody PDFTransactionDTO pdfTransactionDTO) throws Exception {
        Client clientCurrent = clientService.finByEmail(authentication.getName());
        Account accountCurrent = accountService.findByNumber(pdfTransactionDTO.getAccountNumber());
        Set<Transaction> transactionsCurrent = accountCurrent.getTransactions();

        if(clientCurrent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(accountCurrent == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(pdfTransactionDTO.getStartDate() == null || pdfTransactionDTO.getEndDate() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(pdfTransactionDTO.getStartDate().isAfter(pdfTransactionDTO.getEndDate())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Set<Transaction> transactions = transactionService.getTransactionByDate(pdfTransactionDTO.getStartDate(), pdfTransactionDTO.getEndDate(), transactionsCurrent);

        CreatePDF.generatePDF(transactions, clientCurrent, response);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}