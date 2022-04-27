package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping ("/tenmo/")
public class AccountController {

    private AccountDao accountDao;

    public AccountController (AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @RequestMapping (path = "balance", method = RequestMethod.GET)
    public BigDecimal getBalance (Principal principal) {
        return accountDao.getBalance(principal.getName());
    }


}
