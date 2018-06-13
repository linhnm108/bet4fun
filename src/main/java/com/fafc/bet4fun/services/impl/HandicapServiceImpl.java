package com.fafc.bet4fun.services.impl;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.repositories.HandicapRepository;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.HandicapService;

@Service
public class HandicapServiceImpl implements HandicapService {

    @Autowired
    HandicapRepository handicapRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    AuthenticationService authenticationService;

    @Override
    public Handicap saveHandicap(Handicap handicap) {
        return this.handicapRepository.save(handicap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Handicap> getAllUpcomingHandicaps() {
        Client currentUser = this.authenticationService.getLoggedInUser();
        Query query = entityManager.createNativeQuery("SELECT * FROM handicap WHERE expired_date > current_date AND client_id != ? ORDER BY expired_date ASC", Handicap.class);
        query.setParameter(1, currentUser.getClientId());
        return query.getResultList();
    }

    @Override
    public Handicap findById(String uuid) {
        return this.handicapRepository.findById(UUID.fromString(uuid)).get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Handicap> getAllHandicapsOfCurrentUser() {
        Client currentUser = this.authenticationService.getLoggedInUser();
        Query query = entityManager.createNativeQuery("SELECT * FROM handicap WHERE client_id = ? ORDER BY expired_date DESC", Handicap.class);
        query.setParameter(1, currentUser.getClientId());
        return query.getResultList();
    }

}
