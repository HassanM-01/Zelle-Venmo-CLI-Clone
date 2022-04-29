package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer {

    private Long userId;
    private BigDecimal transferAmount;
    private Long transferType;
    private Long transferStatus;
    private Long transferId;
    private String toUsername;
    private String fromUsername;

    public Transfer(Long userId, BigDecimal transferAmount, Long transferType, Long transferStatus, Long transferId, String toUsername, String fromUsername) {
        this.userId = userId;
        this.transferAmount = transferAmount;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.transferId = transferId;
        this.toUsername = toUsername;
        this.fromUsername = fromUsername;
    }

    public Transfer(){}

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String username) {
        this.toUsername = username;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Long getTransferType() {
        return transferType;
    }

    public void setTransferType(Long transferType) {
        this.transferType = transferType;
    }

    public Long getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Long transferStatus) {
        this.transferStatus = transferStatus;
    }

}
