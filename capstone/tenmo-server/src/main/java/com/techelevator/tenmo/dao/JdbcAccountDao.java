package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Map;


@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public  JdbcAccountDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
     }

    @Override
    public BigDecimal getBalance(String username) {
        BigDecimal balance = new BigDecimal(0);
        String sql = "SELECT balance FROM account JOIN tenmo_user USING (user_id) WHERE username = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);

        if (results.next()){
            balance = results.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public boolean sendFunds(BigDecimal bigDecimal, Long id) {
        String sql = "UPDATE account SET balance = balance - ? WHERE user_id = ? AND balance >= ?";

        return jdbcTemplate.update(sql, bigDecimal, id, bigDecimal) == 1;
    }

    @Override
    public boolean recieveFunds(BigDecimal bigDecimal, Long id) {
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";

        return jdbcTemplate.update(sql, bigDecimal, id) == 1;
    }


    private Account mapRowToAccount (SqlRowSet result){
        Account account = new Account();
        account.setUserId(result.getLong("user_id"));

        return account;
    }


}
