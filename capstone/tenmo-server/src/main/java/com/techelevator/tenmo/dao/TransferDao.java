package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public boolean logTransfer(Transfer transfer, String username);

    public List<Transfer> getTransfers (String username);

    boolean logRequestTransfer(Long currentUserId, Transfer transfer);

}
