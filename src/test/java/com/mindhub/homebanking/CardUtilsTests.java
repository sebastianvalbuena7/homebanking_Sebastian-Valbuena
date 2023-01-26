package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated() {
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberContains(){
        String cardNumber = CardUtils.getCardNumber();
        assertThat(cardNumber, containsString("-"));
    }

    @Test
    public void cvvNumberIsCreated() {
        int cvvNumber = CardUtils.getCvv();
        assertThat(cvvNumber, is(notNullValue()));
    }

    @Test
    public void cvvNumber() {
        int cvvNumber = CardUtils.getCvv();
        assertThat(cvvNumber, lessThan(1000));
    }
}