package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Long userId;
    private BigDecimal transferAmount;
    private Long transferType;
    private Long transferId;
    private Long transferStatus;
    private String toUsername;
    private String fromUsername;

    public Transfer(Long userId, BigDecimal transferAmount, Long transferType) {
        this.userId = userId;
        this.transferAmount = transferAmount;
        this.transferType = transferType;
    }

    public Transfer (BigDecimal transferAmount, Long transferStatus,Long transferType, Long userId, Long transferId, String fromUsername){
        this.transferAmount = transferAmount;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
        this.userId = userId;
        this.transferId = transferId;
        this.fromUsername = fromUsername;
    }

    public Transfer(){}

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Long transferStatus) {
        this.transferStatus = transferStatus;
    }

    public long getUserId() {
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

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
}
