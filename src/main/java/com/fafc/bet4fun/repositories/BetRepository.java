package com.fafc.bet4fun.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.fafc.bet4fun.entities.Bet;

public interface BetRepository extends CrudRepository<Bet, UUID> {
}
