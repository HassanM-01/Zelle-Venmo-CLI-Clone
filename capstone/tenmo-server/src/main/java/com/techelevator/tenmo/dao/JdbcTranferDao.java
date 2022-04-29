package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTranferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTranferDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    //might need to add datasource


    @Override
    public boolean logTransfer(Transfer transfer, String username) {
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao(jdbcTemplate.getDataSource());

        String tranferLogSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) " +
                "VALUES (2, 1, (SELECT account_id FROM account JOIN tenmo_user USING (user_id) WHERE username = ?), (SELECT account_id FROM account WHERE user_id = ?), ?) RETURNING transfer_id";


        Long id = jdbcTemplate.queryForObject(tranferLogSql, Long.class, username, transfer.getUserId(), transfer.getTransferAmount());
        int userId = jdbcUserDao.findIdByUsername(username);
        Long parsedUserId = Long.parseLong("" + userId);

        String updateSentSql = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";


        if (jdbcAccountDao.sendFunds(transfer.getTransferAmount(), parsedUserId)) {
            jdbcAccountDao.recieveFunds(transfer.getTransferAmount(), transfer.getUserId());
            jdbcTemplate.update(updateSentSql, id);
            return true;

        } else {

            String updateTransferFailedSql = "UPDATE transfer SET transfer_status_id = 3 WHERE transfer_id = ?";
            jdbcTemplate.update(updateTransferFailedSql, id);
        }

        return false;
    }

    @Override
    public List<Transfer> getTransfers(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql =  "SELECT transfer_id " +
                ", transfer_type_id " +
                ", transfer_status_id " + ", amount " +
                ", (SELECT username FROM tenmo_user JOIN account USING (user_id) WHERE account_id = transfer.account_from) AS user_from " +
                ", (SELECT username FROM tenmo_user JOIN account USING (user_id) WHERE account_id = transfer.account_to) AS user_to " +
                "FROM transfer " +
                "WHERE account_from = (SELECT account_id FROM account JOIN tenmo_user USING (user_id) WHERE username = ?) " +
                "OR account_to = (SELECT account_id FROM account JOIN tenmo_user USING (user_id) WHERE username = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    private Transfer mapRowToTransfer (SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getLong("transfer_id"));
        transfer.setTransferType(rowSet.getLong("transfer_type_id"));
        transfer.setTransferStatus(rowSet.getLong("transfer_status_id"));
        transfer.setTransferAmount(rowSet.getBigDecimal("amount"));
        transfer.setToUsername(rowSet.getString("user_to"));
        transfer.setFromUsername(rowSet.getString("user_from"));
        return transfer;
    }




}
