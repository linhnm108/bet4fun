package com.fafc.bet4fun.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.Bet;
import com.fafc.bet4fun.repositories.BetRepository;
import com.fafc.bet4fun.services.BetService;

@Service
public class BetServiceImpl implements BetService {
    @Autowired
    BetRepository betRepository;

    @Override
    public Bet saveBet(Bet bet) {
        return this.betRepository.save(bet);
    }

}
