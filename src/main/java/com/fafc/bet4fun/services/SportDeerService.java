package com.fafc.bet4fun.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fafc.bet4fun.api_models.SportDeer;
import com.fafc.bet4fun.api_models.Token;

@Service
public class SportDeerService {

    private static final String SPORT_DEER_BASE_URL = "https://api.sportdeer.com/v1";
    private static final String SPORT_DEER_ACCESS_TOKEN_URL = "/accessToken?refresh_token=";
    private static final String SPORT_DEER_COMING_UP_URL = "/upcoming?access_token=";

    @Autowired
    RestTemplate restTemplate;

    public Token getAccessToken(String refreshToken) {
        String accessTokenUrl = SPORT_DEER_BASE_URL + SPORT_DEER_ACCESS_TOKEN_URL + refreshToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(accessTokenUrl, HttpMethod.GET, requestEntity, Token.class).getBody();
    }

    public SportDeer getUpcommingMatches(String accessToken, String fromDate, String toDate) {
        String accessTokenUrl = SPORT_DEER_BASE_URL + SPORT_DEER_COMING_UP_URL + accessToken + "&dateFrom=" + fromDate + "&dateTo=" + toDate;
        return this.restTemplate.getForObject(accessTokenUrl, SportDeer.class);
    }

}
