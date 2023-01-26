package com.mindhub.homebanking.services.implementations;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void saveAllTransactions(List<Transaction> transactions) {
        transactionRepository.saveAll(transactions);
    }

    @Override
    public Set<Transaction> getTransactionByDate (LocalDateTime dateFrom, LocalDateTime dateTo, Set<Transaction> transactionsAuth) {
        return transactionsAuth.stream()
                .filter(t -> t.getDate().isAfter(dateFrom) && t.getDate().isBefore(dateTo))
                .collect(Collectors.toSet());
    }
}
