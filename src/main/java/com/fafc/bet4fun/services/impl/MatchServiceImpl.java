package com.fafc.bet4fun.services.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Match;
import com.fafc.bet4fun.repositories.MatchRepository;
import com.fafc.bet4fun.services.MatchService;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepository matchRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    @SuppressWarnings("unchecked")
    @Override
    public List<Match> getAllUpcomingMatches() {
        Query query = entityManager.createNativeQuery("SELECT * FROM match WHERE status = ? ORDER BY schedule_date ASC", Match.class);
        query.setParameter(1, Constants.MATCH_NOT_STARTED_STATUS);

        return query.getResultList();
    }

    @Override
    public Match findById(String uuid) {
        return this.matchRepository.findById(UUID.fromString(uuid)).get();
    }

}
