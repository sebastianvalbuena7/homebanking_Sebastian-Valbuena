package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientService.findAllClients().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return new ClientDTO(clientService.getClientById(id));
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String password, @RequestParam String email) {
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Data incomplete", HttpStatus.FORBIDDEN);
        }
        if(clientService.finByEmail(email) != null) {
            return new ResponseEntity<>("Already name is used", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, passwordEncoder.encode(password) ,email);
        clientService.saveClient(client);

        Account account = new Account("VIN" + (int) ((Math.random() * (999999 - 100000)) + 100000), LocalDateTime.now(), 0, client, AccountType.SAVING, true);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientService.finByEmail(authentication.getName()));
    }

    @GetMapping("/client/card")
    public Set<CardDTO> getCardsClient(Authentication authentication) {
        Client client = clientService.finByEmail(authentication.getName());
        return client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }
}