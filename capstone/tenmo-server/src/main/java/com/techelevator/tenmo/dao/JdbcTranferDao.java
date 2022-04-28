package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Component
public class JdbcTranferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTranferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean sendFunds(Transfer transfer, String username) {
        boolean success = false;
        String tranferLogSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                               "account_from, account_to, amount) " +
                               "VALUES (2, 1, (SELECT account_id FROM account JOIN tenmo_user USING (user_id) WHERE username = ?), (SELECT account_id FROM account WHERE user_id = ?), ?) RETURNING transfer_id";
        Long id = jdbcTemplate.queryForObject(tranferLogSql, Long.class, username, transfer.getUserId(), transfer.getTransferAmount());
        String sql = "BEGIN TRANSACTION;" +
                     "UPDATE account SET balance = balance - ? WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?) AND balance > ?;" +
                     "UPDATE account SET balance = balance + ? WHERE user_id = ?;" +
                     "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?;" +
                     "COMMIT;";
        return jdbcTemplate.update(sql, transfer.getTransferAmount(), username, transfer.getTransferAmount(), transfer.getTransferAmount(), transfer.getUserId(), id) == 1;
    }

}
