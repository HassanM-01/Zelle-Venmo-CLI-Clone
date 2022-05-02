package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class JdbcTranferDaoTest {

    private static SingleConnectionDataSource dataSource;
    private JdbcTranferDao jdbcTranferDao;

    @BeforeClass
    public static void setup(){
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit(false);
    }

    @Before
    public void setupData(){
        //Create test users
        String userOneSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
                "VALUES (1010, 'user1', '$2a$10$E3L5F3XzWQdn/qeN5zgZg.mc3.wZBjfGoGNHbT8OfXB3K03n6U/py')";
        String userTwoSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
                "VALUES (1011, 'user2', '$2a$10$5JXQ7khJSFUJ6SV3DlZlHO4KRtcP6y6WvaFu7Tf18PJMKyEpTSZJ6')";
        String userThreeSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
                "VALUES (1012, 'user3', 'HASH')";
        //Create test accounts
        String userOneAccountSql = "INSERT INTO account (account_id, user_id, balance) " +
                "VALUES (2010, 1010, 1000.00);";
        String userTwoAccountSql = "INSERT INTO account (account_id, user_id, balance) " +
                "VALUES (2011, 1011, 599.00)";
        String userThreeAccountSql = "INSERT INTO account (account_id, user_id, balance) " +
                "VALUES (2012, 1012, 400.00)";
        //Create test transfers
        String transferOneSql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (3080, 1, 1, 2010, 2011, 5000.00)";
        String transferTwoSql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (3081, 1, 2, 2011, 2010, 65.00)";
        String transferThreeSql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (3082, 2, 2, 2010, 2011, 85.00)";
        String transferFourSql = "INSERT INTO transfer (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (3083, 2, 2, 2012, 2011, 90.00)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(userOneSql);
        jdbcTemplate.update(userTwoSql);
        jdbcTemplate.update(userThreeSql);
        jdbcTemplate.update(userOneAccountSql);
        jdbcTemplate.update(userTwoAccountSql);
        jdbcTemplate.update(userThreeAccountSql);
        jdbcTemplate.update(transferOneSql);
        jdbcTemplate.update(transferTwoSql);
        jdbcTemplate.update(transferThreeSql);
        jdbcTemplate.update(transferFourSql);

        jdbcTranferDao = new JdbcTranferDao(dataSource);
    }

    @Test
    public void logTransfer() {
        String currentUserName = "user1";
        Long currentUserId = (long) 1010;
        Transfer newTransfer = new Transfer();
        newTransfer.setUserIdOfDestinationAccount((long) 1011);
        newTransfer.setTransferAmount(BigDecimal.valueOf(50.00));
        newTransfer.setTransferType((long) 1);

        jdbcTranferDao.logSendTransfer(newTransfer, currentUserId);
        List<Transfer> transfers = jdbcTranferDao.getTransfers(currentUserName);
        int expected = 4;
        Assert.assertEquals(expected, transfers.size());
    }

    @Test
    public void getTransferMethodCorrectlyReturnsAListOfSize3() {
        List<Transfer> transfers = jdbcTranferDao.getTransfers("user1");
        Assert.assertEquals(3, transfers.size());
    }

    @Test
    public void transferAtIndex0InListHasAmountValue50() {
        List<Transfer> transfers = jdbcTranferDao.getTransfers("user1");
        String expected = "5000.00";
        Assert.assertEquals(transfers.get(0).getTransferAmount().toString(), expected);
    }

    @Test
    public void transferAtIndex1InListHasAmountValue50() {
        List<Transfer> transfers = jdbcTranferDao.getTransfers("user1");
        String expected = "65.00";
        Assert.assertEquals(transfers.get(1).getTransferAmount().toString(), expected);
    }

    @Test
    public void transferListOnlyDisplaysTransferThatTheUserIsAssociatedWith() {
        List<Transfer> transfers = jdbcTranferDao.getTransfers("user3");
        int expected = 1;
        Assert.assertEquals(expected, transfers.size());
    }

    @Test
    public void requestDelinedUpdatesStatusFromPendingToRejected() {
        Long expected = (long) 3;
        jdbcTranferDao.requestRejected((long) 3080);
        List<Transfer> transfers = jdbcTranferDao.getTransfers("user1");
        Assert.assertEquals(expected, transfers.get(2).getTransferStatus());
    }

    //Having some strange issues with the way the list populates for this test
    @Test
    public void requestDelinedUpdatesStatusFromPendingToRejectedForTransfer2() {
        Long expected = (long) 3;
        jdbcTranferDao.requestRejected((long) 3081);
        Transfer transfer = jdbcTranferDao.getTransferById((long) 3081);
        Assert.assertEquals(transfer.getTransferStatus(), expected);
    }

    @Test
    public void requestApprovedSetsStatusToRejectedIfThereInsufficientFunds() {
        Transfer transfer = jdbcTranferDao.getTransferById((long)3080);
        jdbcTranferDao.requestApproved(transfer, (long) 1011, (long) 3080);
        Long expected = (long) 3;
        Transfer returnedTransfer = jdbcTranferDao.getTransferById((long)3080);
        Assert.assertEquals(expected, returnedTransfer.getTransferStatus());
    }

    //Only used for testing purposes
    @Test
    public void getTransferReturnsTransfer() {
        Transfer transfer = jdbcTranferDao.getTransferById((long) 3080);
        Assert.assertEquals(transfer.getTransferAmount().toString(), "5000.00");
    }



    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

    @AfterClass
    public static void closeDataSource(){
        dataSource.destroy();
    }

}
