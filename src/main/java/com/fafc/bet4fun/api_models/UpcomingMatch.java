package com.fafc.bet4fun.api_models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpcomingMatch {

    @JsonProperty
    private long _id;

    @JsonProperty
    private long id_country;

    @JsonProperty
    private long id_league;

    @JsonProperty
    private long id_season;

    @JsonProperty
    private long id_stage;

    @JsonProperty
    private String fixture_status;

    @JsonProperty
    private String fixture_status_short;

    @JsonProperty
    private String id_team_season_away;

    @JsonProperty
    private String id_team_season_home;

    @JsonProperty
    private String number_goal_team_away;

    @JsonProperty
    private String number_goal_team_home;

    @JsonProperty
    private String round;

    @JsonProperty
    private String schedule_date;

    @JsonProperty
    private String stadium;

    @JsonProperty
    private String team_season_away_name;

    @JsonProperty
    private String team_season_home_name;

}