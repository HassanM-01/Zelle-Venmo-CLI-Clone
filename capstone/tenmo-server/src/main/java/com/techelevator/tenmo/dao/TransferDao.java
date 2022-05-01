package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public boolean logSendTransfer(Transfer transfer, Long currentUserId);

    public List<Transfer> getTransfers (String username);

    boolean logRequestTransfer(Long currentUserId, Transfer transfer);

    boolean requestRejected(Long id);

    boolean requestApproved(Transfer transfer, Long userId, Long transferId);

}
