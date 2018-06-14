package com.fafc.bet4fun.api_models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Token {
    @JsonProperty
    private String new_access_token;
    @JsonProperty
    private String expire_date;
    @JsonProperty
    private String expires_in;
    @JsonProperty
    private long expires_in_ms;
}
