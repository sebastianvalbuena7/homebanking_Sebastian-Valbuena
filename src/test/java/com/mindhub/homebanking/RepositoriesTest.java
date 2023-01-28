package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    // Tests de Integracion
//    @Autowired
//    LoanRepository loanRepository;
//
//    @Autowired
//    TransactionRepository transactionRepository;
//
//    @Autowired
//    CardRepository cardRepository;
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Autowired
//    ClientRepository clientRepository;
//
//    @Test
//    public void existLoans() {
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans, is(not(empty())));
//    }
//
//    @Test
//    public void existPersonalLoan() {
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
//    }
//
//    @Test
//    public void existCard() {
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards, is(not(empty())));
//    }
//
//    @Test
//    public void existColorCard() {
//        List<Card> cards = cardRepository.findAll().stream().filter(card -> card.getColor() == ColorType.SILVER).collect(Collectors.toList());
//        assertThat(cards, is(not(empty())));
//    }
//
//    @Test
//    public void existAdminClient(){
//        Client client = clientRepository.findByEmail("admin@correo.com");
//        assertThat(client, is(notNullValue()));
//    }
//
//    @Test
//    public void existClients() {
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, is(not(empty())));
//    }
//
//    @Test
//    public void existTransactionById() {
//        Transaction transaction = transactionRepository.findById(38l).orElse(null);
//        assertThat(transaction, is(notNullValue()));
//    }
//
//    @Test
//    public void existTypeTransaction() {
//        List<Transaction> transactions = transactionRepository.findAll().stream().filter(transaction1 -> transaction1.getType() == TransactionType.CREDIT).toList();
//        assertThat(transactions, is(not(empty())));
//    }
//
//    @Test
//    public void existAccountByBalance() {
//        List<Account> accounts = accountRepository.findAll().stream().filter(account1 -> account1.getBalance() > 150000).toList();
//        assertThat(accounts, is(not(empty())));
//    }
//
//    @Test
//    public void account() {
//        Account account = accountRepository.findByNumber("VIN418296");
//        assertThat(account, is(notNullValue()));
//    }

    // Tests unitarios

}