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
    public boolean logSendTransfer(Transfer transfer, Long currentUserId) {
        String tranferLogSql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount) " +
                "VALUES (?, 1, (SELECT account_id FROM account WHERE user_id = ?), " +
                "(SELECT account_id FROM account WHERE user_id = ?), ?) RETURNING transfer_id";

        Long returnedTransferId = jdbcTemplate.queryForObject(tranferLogSql, Long.class, transfer.getTransferType(),
                currentUserId, transfer.getUserIdOfDestinationAccount(), transfer.getTransferAmount());

        return requestApproved(transfer, currentUserId, returnedTransferId);
    }

    @Override
    public boolean requestApproved(Transfer transfer, Long currentUserId, Long returnedTransferId) {
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao(jdbcTemplate.getDataSource());
        // Query updates transfer to approved
        String updateSentSql = "UPDATE transfer SET transfer_status_id = 2 WHERE transfer_id = ?";
        if (jdbcAccountDao.sendFunds(transfer.getTransferAmount(), currentUserId)) {
            jdbcAccountDao.recieveFunds(transfer.getTransferAmount(), transfer.getUserIdOfDestinationAccount());
            jdbcTemplate.update(updateSentSql, returnedTransferId);
            return true;
        } else {
            requestRejected(returnedTransferId);
            return false;
        }

    }

    @Override
    public boolean requestRejected(Long transferId) {
        String updateTransferFailedSql = "UPDATE transfer SET transfer_status_id = 3 WHERE transfer_id = ?";
        return jdbcTemplate.update(updateTransferFailedSql, transferId) == 1;
    }

    @Override
    public List<Transfer> getTransfers(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql =  "SELECT transfer_id " +
                ", transfer_type_id " +
                ", transfer_status_id " + ", amount " +
                ", (SELECT username FROM tenmo_user JOIN account USING (user_id) WHERE account_id = transfer.account_from) AS user_from " +
                ", (SELECT username FROM tenmo_user JOIN account USING (user_id) WHERE account_id = transfer.account_to) AS user_to " +
                ", (SELECT user_id FROM account WHERE account_id = transfer.account_to) AS user_id " +
                "FROM transfer " +
                "WHERE account_from = (SELECT account_id FROM account JOIN tenmo_user USING (user_id) WHERE username = ?) " +
                "OR account_to = (SELECT account_id FROM account JOIN tenmo_user USING (user_id) WHERE username = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username);

        while(results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    public Transfer getTransferById(Long transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id " +
                ", transfer_type_id " +
                ", transfer_status_id " + ", amount " +
                ", (SELECT username FROM tenmo_user JOIN account USING (user_id) WHERE account_id = transfer.account_from) AS user_from " +
                ", (SELECT username FROM tenmo_user JOIN account USING (user_id) WHERE account_id = transfer.account_to) AS user_to " +
                ", (SELECT user_id FROM account WHERE account_id = transfer.account_to) AS user_id " +
                "FROM transfer " +
                "WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        if (result.next()) {
            transfer = mapRowToTransfer(result);
        }
        return transfer;
    }

    @Override
    public boolean logRequestTransfer(Long currentUserId, Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, 1, (SELECT account_id FROM account WHERE user_id = ?), " +
                "(SELECT account_id FROM account WHERE user_id = ?), ?)";

        return jdbcTemplate.update(sql, transfer.getTransferType(), transfer.getUserIdOfDestinationAccount(), currentUserId, transfer.getTransferAmount()) == 1;
    }

    private Transfer mapRowToTransfer (SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getLong("transfer_id"));
        transfer.setTransferType(rowSet.getLong("transfer_type_id"));
        transfer.setTransferStatus(rowSet.getLong("transfer_status_id"));
        transfer.setTransferAmount(rowSet.getBigDecimal("amount"));
        transfer.setToUsername(rowSet.getString("user_to"));
        transfer.setFromUsername(rowSet.getString("user_from"));
        transfer.setUserIdOfDestinationAccount(rowSet.getLong("user_id"));
        return transfer;
    }




}
