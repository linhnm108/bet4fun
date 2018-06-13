package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Handicap;

public interface HandicapService {
    public Handicap saveHandicap(Handicap handicap);
    public Handicap findById(String uuid);
    public List<Handicap> getAllUpcomingHandicaps();
    public List<Handicap> getAllHandicapsOfCurrentUser();
}
