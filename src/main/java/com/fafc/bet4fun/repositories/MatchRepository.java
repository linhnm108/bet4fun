package com.fafc.bet4fun.repositories;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.fafc.bet4fun.entities.Match;

public interface MatchRepository extends CrudRepository<Match, UUID> {
}
