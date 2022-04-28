package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer {

    private Long userId;
    private BigDecimal transferAmount;
    private Long transferType;
    private Long transferStatus;
    private Long transferId;

    public Transfer(Long userId, BigDecimal transferAmount, Long transferType, Long transferStatus, Long transferId) {
        this.userId = userId;
        this.transferAmount = transferAmount;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.transferId = transferId;
    }

    public Transfer(){}



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
