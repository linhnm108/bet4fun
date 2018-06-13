package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Handicap;

public interface HandicapService {
    public Handicap saveHandicap(Handicap handicap);
    public List<Handicap> getAllUpcomingHandicaps();
}
