package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Match;

public interface MatchService {
    public Match saveMatch(Match match);
    public List<Match> getAllMatches();
    public void deleteMatch(String uuid);
}
