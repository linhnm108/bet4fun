package com.fafc.bet4fun.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.repositories.HandicapRepository;
import com.fafc.bet4fun.services.HandicapService;

@Service
public class HandicapServiceImpl implements HandicapService {

    @Autowired
    HandicapRepository handicapRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Handicap saveHandicap(Handicap handicap) {
        return this.handicapRepository.save(handicap);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Handicap> getAllUpcomingHandicaps() {
        Query query = entityManager.createNativeQuery("SELECT * FROM handicap WHERE result IS NULL AND expired_date > current_date ORDER BY expired_date ASC", Handicap.class);

        return query.getResultList();
    }

}
