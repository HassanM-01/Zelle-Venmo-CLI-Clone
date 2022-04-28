package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountServices {

    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;
    private String API_BASE_URL;

    public AccountServices(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.API_BASE_URL = url;
    }


    public BigDecimal returnBalance (){
    BigDecimal balance = new BigDecimal(0);
    try {
            balance = restTemplate.exchange(API_BASE_URL + "tenmo/balance/", HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
    } catch (RestClientResponseException | ResourceAccessException e) {
        BasicLogger.log(e.getMessage());
    }
    return balance;
    }

    public User[] getUsers () {
        User[] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "tenmo/users/list", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }




    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }


}
