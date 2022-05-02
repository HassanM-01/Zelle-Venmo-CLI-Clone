package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Long userIdOfDestinationAccount;
    private BigDecimal transferAmount;
    private Long transferType;
    private Long transferStatus;
    private Long transferId;
    private String toUsername;
    private String fromUsername;
    private Long accountFromId;
    private Long accountToId;

    public Transfer(Long transferId, Long transferType, Long transferStatus, Long accountFromId, Long accountToId, BigDecimal transferAmount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.transferAmount = transferAmount;
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

    public Long getUserIdOfDestinationAccount() {
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

    public Long getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Long transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(Long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public Long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(Long accountToId) {
        this.accountToId = accountToId;
    }
}
