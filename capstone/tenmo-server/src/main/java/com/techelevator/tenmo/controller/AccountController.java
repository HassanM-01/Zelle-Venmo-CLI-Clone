package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping ("/tenmo/")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @ApiOperation(value = "Retrieves current balance based on current user")
    @RequestMapping (path = "balance", method = RequestMethod.GET)
    public BigDecimal getBalance (Principal principal) {
        return accountDao.getBalance(principal.getName());
    }

    @ApiOperation(value = "Returns list of registered users that doesn't include current user")
    @RequestMapping(path = "users/list", method = RequestMethod.GET)
    public List<User> getAllUsers (Principal principal) {
        return userDao.findAll(principal.getName());
    }

}
