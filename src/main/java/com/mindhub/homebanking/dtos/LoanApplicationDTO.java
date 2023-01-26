package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private double amount;
    private int payment;
    private String numberDestiny;

    public LoanApplicationDTO(long id, double amount, int payment, String numberDestiny) {
        this.id = id;
        this.amount = amount;
        this.payment = payment;
        this.numberDestiny = numberDestiny;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayment() {
        return payment;
    }

    public String getNumberDestiny() {
        return numberDestiny;
    }
}