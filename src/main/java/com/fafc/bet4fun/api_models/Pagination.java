package com.fafc.bet4fun.api_models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pagination {
    @JsonProperty
    private int total;

    @JsonProperty
    private int limit;

    @JsonProperty
    private int page;

    @JsonProperty
    private int pages;
}
