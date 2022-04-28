package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    public boolean sendFunds(Transfer transfer, String username);

}
