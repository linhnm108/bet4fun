package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Bet;

public interface BetService {
    public Bet saveBet(Bet bet);
    public List<Bet> getAllBetsOfCurrentUser();
}
