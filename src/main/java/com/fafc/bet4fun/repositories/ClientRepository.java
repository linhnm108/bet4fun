package com.fafc.bet4fun.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.fafc.bet4fun.entities.Client;

public interface ClientRepository extends CrudRepository<Client, UUID> {
    public Client findByClientName(String clientName);
}
