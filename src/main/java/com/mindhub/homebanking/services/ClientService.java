package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> findAllClients();

    Client getClientById(Long id);

    void saveClient(Client client);

    Client finByEmail(String email);
}