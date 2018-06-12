package com.fafc.bet4fun.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.Handicap;
import com.fafc.bet4fun.repositories.HandicapRepository;
import com.fafc.bet4fun.services.HandicapService;

@Service
public class HandicapServiceImpl implements HandicapService {

    @Autowired
    HandicapRepository handicapRepository;

    @Override
    public Handicap saveHandicap(Handicap handicap) {
        return this.handicapRepository.save(handicap);
    }

}
