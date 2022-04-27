package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.Map;

public interface AccountDao {

    BigDecimal getBalance(String username);

    public Map<Long, String> getAllAccounts();




}
