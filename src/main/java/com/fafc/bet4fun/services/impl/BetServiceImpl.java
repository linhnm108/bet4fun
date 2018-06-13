package com.fafc.bet4fun.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.Bet;
import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.repositories.BetRepository;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.BetService;

@Service
public class BetServiceImpl implements BetService {
    @Autowired
    BetRepository betRepository;

    @Autowired
    AuthenticationService authenticationService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Bet saveBet(Bet bet) {
        return this.betRepository.save(bet);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Bet> getAllBetsOfCurrentUser() {
        Client currentUser = this.authenticationService.getLoggedInUser();
        Query query = entityManager.createNativeQuery("SELECT b1.* FROM bet b1 INNER JOIN handicap h1 ON b1.handicap_id = h1.handicap_id WHERE b1.client_id = ? ORDER BY h1.expired_date DESC", Bet.class);
        query.setParameter(1, currentUser.getClientId());
        return query.getResultList();
    }

}
