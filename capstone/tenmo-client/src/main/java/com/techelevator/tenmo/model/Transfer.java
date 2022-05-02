package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Long userIdOfDestinationAccount;
    private BigDecimal transferAmount;
    private Long transferType;
    private Long transferId;
    private Long transferStatus;
    private String toUsername;
    private String fromUsername;

    public Transfer(Long userIdOfDestinationAccount, BigDecimal transferAmount, Long transferType) {
        this.userIdOfDestinationAccount = userIdOfDestinationAccount;
        this.transferAmount = transferAmount;
        this.transferType = transferType;
    }

    public Transfer (BigDecimal transferAmount, Long transferStatus, Long transferType, Long userIdOfDestinationAccount, Long transferId, String fromUsername){
        this.transferAmount = transferAmount;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
        this.userIdOfDestinationAccount = userIdOfDestinationAccount;
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

    public long getUserIdOfDestinationAccount() {
        return userIdOfDestinationAccount;
    }

    public void setUserIdOfDestinationAccount(Long userIdOfDestinationAccount) {
        this.userIdOfDestinationAccount = userIdOfDestinationAccount;
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
