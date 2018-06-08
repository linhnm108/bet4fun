package com.fafc.bet4fun.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.Match;
import com.fafc.bet4fun.repositories.MatchRepository;
import com.fafc.bet4fun.services.MatchService;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Override
    public Match saveMatch(Match match) {
        return this.matchRepository.save(match);
    }

    @Override
    public List<Match> getAllMatches() {
        return (List<Match>) this.matchRepository.findAll();
    }

    @Override
    public void deleteMatch(String uuid) {
        this.matchRepository.deleteById(UUID.fromString(uuid));
    }

}
