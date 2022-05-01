package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("tenmo/transfer/")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;


    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @ApiOperation(value = "logs a new transfer and sends funds if available")
    @RequestMapping(path = "send", method = RequestMethod.POST)
    public boolean sendFunds(Principal principal, @ApiParam(value = "transfer that includes destination user id, amount, transfer type", required = true) @RequestBody Transfer transfer) {
        Long currentUserId = (long) userDao.findIdByUsername(principal.getName());
        return transferDao.logSendTransfer(transfer, currentUserId);
    }

    @ApiOperation(value = "Retrieves list of all transfers")
    @RequestMapping(path = "list", method = RequestMethod.GET)
    public List<Transfer> getTransfers(Principal principal) {
        return transferDao.getTransfers(principal.getName());
    }

    @ApiOperation(value = "Logs a new transfer request")
    @RequestMapping(path = "request", method = RequestMethod.POST)
    public boolean requestFunds(Principal principal, @ApiParam(value = "A transfer object that includes all parameters", required = true) @RequestBody Transfer transfer) {
        Long currentUserId = (long) userDao.findIdByUsername(principal.getName());
        return transferDao.logRequestTransfer(currentUserId, transfer);
    }

    @ApiOperation(value = "Updates transfer to approved if the user has sufficient funds")
    @RequestMapping(path = "approve", method = RequestMethod.PUT)
    public boolean approveRequest(Principal principal, @ApiParam(value = "A transfer object that includes all parameters", required = true) @RequestBody Transfer transfer) {
        Long currentUserId = (long) userDao.findIdByUsername(principal.getName());
        return transferDao.requestApproved(transfer, currentUserId, transfer.getTransferId());
    }

    @ApiOperation(value = "Updates transfer request to declined")
    @RequestMapping(path = "decline", method = RequestMethod.PUT)
    public boolean declineRequest(@ApiParam(value = "A transfer object that includes all parameters", required = true) @RequestBody Transfer transfer) {
        return transferDao.requestRejected(transfer.getTransferId());
    }


}
