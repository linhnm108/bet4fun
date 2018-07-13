package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Handicap;

public interface HandicapService {
    public Handicap saveHandicap(Handicap handicap);
    public Handicap findById(String uuid);
    public List<Handicap> getAll1x2UpcomingHandicaps();
    public List<Handicap> getAllOverUnderUpcomingHandicaps();
    public List<Handicap> getAll1x2HandicapsOfCurrentUser();
    public List<Handicap> getAllOverUnderHandicapsOfCurrentUser();
}