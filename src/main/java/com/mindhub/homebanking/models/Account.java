package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType accountType;
    private boolean showAccount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    @OneToMany(mappedBy="account", fetch= FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    public Account() {
    }

    public Account(String number, LocalDateTime creationDate, double balance, Client client, AccountType accountType, boolean showAccount) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.accountType = accountType;
        this.showAccount = showAccount;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public boolean getShowAccount() {
        return showAccount;
    }

    public void setShowAccount(boolean showAccount) {
        this.showAccount = showAccount;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}