package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountDao {

    BigDecimal getBalance(String username);

//    public User[] getAllAccounts();

    boolean sendFunds (BigDecimal bigDecimal, Long id);

    boolean recieveFunds (BigDecimal bigDecimal, Long id);


}
