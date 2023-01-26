package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.time.LocalDateTime;

public class CLientLoanDTO {
    private long idClient;
    private double amount;
    private int payments;
    private LocalDateTime localDateTime;
    private String name;
    private long idLoan;

    public CLientLoanDTO(ClientLoan clientLoan) {
        this.idClient = clientLoan.getClient().getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.localDateTime = clientLoan.getLocalDateTime();
    }

    public long getIdClient() {
        return idClient;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getName() {
        return name;
    }

    public long getIdLoan() {
        return idLoan;
    }
}