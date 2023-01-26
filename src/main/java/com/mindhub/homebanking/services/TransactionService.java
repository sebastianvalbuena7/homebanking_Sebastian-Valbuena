package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
    void saveAllTransactions(List<Transaction> transactions);
     public Set<Transaction> getTransactionByDate (LocalDateTime dateFrom, LocalDateTime dateTo, Set<Transaction> transactionsAuth);
}