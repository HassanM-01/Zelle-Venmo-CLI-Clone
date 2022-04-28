package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Long userId;
    private BigDecimal transferAmount;
    private Long transferType;
    private Long transferId;
    private Long transferStatus;

    public Transfer(Long userId, BigDecimal transferAmount, Long transferType) {
        this.userId = userId;
        this.transferAmount = transferAmount;
        this.transferType = transferType;
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

    public Transfer(){}

    public Transfer (BigDecimal transferAmount, Long transferStatus,Long transferType, Long userId, Long transferId){
        this.transferAmount = transferAmount;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
        this.userId = userId;
        this.transferId = transferId;
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


}
