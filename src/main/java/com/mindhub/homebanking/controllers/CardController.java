package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication, @RequestParam CardType tipo, @RequestParam ColorType color, @RequestParam String account) {
        Client client = clientService.finByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        Account accountCurrent = accountService.findByNumber(account);
        List<Card> cardsType = cards.stream().filter(card -> card.getShowCard()).filter(card -> card.getType() == tipo).toList();

        if(accountCurrent == null) {
          return new ResponseEntity<>("Account invalid",HttpStatus.NOT_FOUND);
        } else if(cards.stream().filter(card -> card.getShowCard()).toList().size() == 6 || cardsType.size() == 3) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            if(cardsType.stream().filter(card1 -> card1.getColor() == color).count() == 1) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                Card card = new Card(LocalDate.now().plusYears(5), LocalDate.now(), CardUtils.getCvv(), CardUtils.getCardNumber(), client.getFirstName().toUpperCase() + " " + client.getLastName().toUpperCase(), color, tipo, client, true, accountCurrent);
                cardService.saveCard(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }
    }

    @PostMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> eliminateCard(Authentication authentication, @PathVariable Long id) {
        Client client = clientService.finByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        Card card = cardService.getCardById(id);
        Card cardFound = cards.stream().filter(card1 -> card1 == card).findFirst().orElse(null);
        cardService.getCardById(cardFound.getId()).setShowCard(false);
        cardService.saveCard(cardFound);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/clients/current/payment")
    public ResponseEntity<Object> paymentsCard(Authentication authentication, @RequestBody CardPaymentDTO cardPaymentDTO) {
        String date = String.valueOf(cardPaymentDTO.getThruDate());
        Client client = clientService.finByEmail(authentication.getName());
        Set<Card> cards = client.getCards();
        Card cardCurrent = cards.stream().filter(card -> card.getNumber().equals(cardPaymentDTO.getNumber())).findFirst().orElse(null);
        String dateCurrent = String.valueOf(cardCurrent.getThruDate());
        Account accountCurrent = accountService.findByNumber(cardCurrent.getAccount().getNumber());
        Account accountDestiny = accountService.findByNumber(cardPaymentDTO.getAccountDestiny());

        if(accountCurrent == accountDestiny) {
            return new ResponseEntity<>("The payment can't equal", HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getNumber().isEmpty()) {
            return new ResponseEntity<>("Missing Account Destiny", HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getAccountDestiny() == null) {
            return new ResponseEntity<>("Invalid Account Destiny", HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getCardHolder().isEmpty()){
            return new ResponseEntity<>("Missing Card Holder",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getAmount() == 0){
            return new ResponseEntity<>("Missing amount",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getNumber().isEmpty()){
            return new ResponseEntity<>("Missing Number",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("Missing description",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getCvv() == 0){
            return new ResponseEntity<>("Missing cvv",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getCvv() != cardCurrent.getCvv()){
            return new ResponseEntity<>("This is not valid cvv",HttpStatus.FORBIDDEN);
        }
        if(cardPaymentDTO.getThruDate() == null) {
            return new ResponseEntity<>("This is not valid date",HttpStatus.FORBIDDEN);
        }
        if(!Objects.equals(date, dateCurrent)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if(LocalDate.now().isAfter(cardCurrent.getThruDate())){
            return new ResponseEntity<>("Expired card",HttpStatus.FORBIDDEN);
        }
        if(cardCurrent.getAccount().getBalance() < cardPaymentDTO.getAmount()){
            return new ResponseEntity<>("Not amount",HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(DEBIT, cardPaymentDTO.getAmount(), cardPaymentDTO.getDescription(), LocalDateTime.now(), accountCurrent, true);
        Transaction transactionDestiny = new Transaction(CREDIT, cardPaymentDTO.getAmount(), cardPaymentDTO.getDescription(), LocalDateTime.now(), accountDestiny, true);
        transaction.setBalanceCurrent(accountCurrent.getBalance() - cardPaymentDTO.getAmount());
        transactionDestiny.setBalanceCurrent(accountDestiny.getBalance() + cardPaymentDTO.getAmount());
        accountCurrent.setBalance(accountCurrent.getBalance() - cardPaymentDTO.getAmount());
        accountDestiny.setBalance(accountDestiny.getBalance() + cardPaymentDTO.getAmount());
        transactionService.saveTransaction(transaction);
        transactionService.saveTransaction(transactionDestiny);
        accountService.saveAccount(accountDestiny);
        accountService.saveAccount(accountCurrent);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}