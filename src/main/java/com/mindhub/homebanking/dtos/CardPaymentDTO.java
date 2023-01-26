package com.mindhub.homebanking.dtos;

import java.time.LocalDate;

public class CardPaymentDTO {
    private String cardHolder;
    private String number;
    private LocalDate thruDate;
    private int cvv;
    private double amount;
    private String description;
    private String accountDestiny;

    public CardPaymentDTO(String cardHolder, String number, LocalDate thruDate, int cvv, double amount, String description, String accountDestiny) {
        this.cardHolder = cardHolder;
        this.number = number;
        this.thruDate = thruDate;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.accountDestiny = accountDestiny;
    }

    public String getAccountDestiny() {
        return accountDestiny;
    }
    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public int getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
