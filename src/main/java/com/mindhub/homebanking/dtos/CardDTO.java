package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.ColorType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private LocalDate thruDate;
    private LocalDate fromDate;
    private int cvv;
    private String number;
    private String cardHolder;
    private ColorType color;
    private CardType type;
    private boolean showCard;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.thruDate = card.getThruDate();
        this.fromDate = card.getFromDate();
        this.cvv = card.getCvv();
        this.number = card.getNumber();
        this.cardHolder = card.getCardHolder();
        this.color = card.getColor();
        this.type = card.getType();
        this.showCard = card.getShowCard();
    }

    public boolean getShowCard() {
        return showCard;
    }

    public long getId() {
        return id;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public int getCvv() {
        return cvv;
    }

    public String getNumber() {
        return number;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public ColorType getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }
}
