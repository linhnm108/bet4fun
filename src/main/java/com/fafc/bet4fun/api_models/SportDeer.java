package com.fafc.bet4fun.api_models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SportDeer {
    @JsonProperty
    List<UpcomingMatch> docs;
    @JsonProperty
    Pagination pagination;
}
