package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;

public class TransferService {

    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;
    private String API_BASE_URL;

    public TransferService(String url, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.API_BASE_URL = url;
    }

    public boolean sendFunds(Transfer transfer) {
        boolean success = false;
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            success = restTemplate.exchange(API_BASE_URL + "tenmo/transfer/send", HttpMethod.POST, entity, Boolean.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean sendTranserRequest(Transfer transfer) {
        boolean success = false;
         HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            success = restTemplate.exchange(API_BASE_URL + "tenmo/transfer/request", HttpMethod.POST, entity, Boolean.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;

    }

    public Transfer[] getTransfers () {
        Transfer[] transfers = null;

        try {
            transfers = restTemplate.exchange(API_BASE_URL + "tenmo/transfer/list", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(Arrays.toString(e.getStackTrace()));
        }
        return transfers;
    }

    public boolean approveTransfer(Transfer transfer) {
        boolean success = false;
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            success = restTemplate.exchange(API_BASE_URL + "tenmo/transfer/approve", HttpMethod.PUT, entity, Boolean.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public boolean declineTransfer(Transfer transfer) {
        boolean success = false;
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            success = restTemplate.exchange(API_BASE_URL + "tenmo/transfer/decline", HttpMethod.PUT, entity, Boolean.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;

    }

    private HttpEntity<Void> makeAuthEntity () {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity (Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }



}
