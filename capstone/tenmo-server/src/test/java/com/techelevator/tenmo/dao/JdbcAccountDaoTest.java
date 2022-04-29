package com.techelevator.tenmo.dao;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class JdbcAccountDaoTest {

    private static SingleConnectionDataSource dataSource;
    private JdbcAccountDao jdbcAccountDao;

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
        String userOneAccountSql = "INSERT INTO account (account_id, user_id, balance) " +
                "VALUES (2010, 1010, 1000.00);";
        String userTwoAccountSql = "INSERT INTO account (account_id, user_id, balance) " +
                "VALUES (2011, 1011, 599.00)";
        String userOneSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
                "VALUES (1010, 'user1', '$2a$10$E3L5F3XzWQdn/qeN5zgZg.mc3.wZBjfGoGNHbT8OfXB3K03n6U/py')";
        String userTwoSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
                "VALUES (1011, 'user2', '$2a$10$5JXQ7khJSFUJ6SV3DlZlHO4KRtcP6y6WvaFu7Tf18PJMKyEpTSZJ6')";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(userOneSql);
        jdbcTemplate.update(userTwoSql);
        jdbcTemplate.update(userOneAccountSql);
        jdbcTemplate.update(userTwoAccountSql);


        jdbcAccountDao = new JdbcAccountDao(dataSource);
    }

    @Test
    public void getBalanceRetrievesStartingBalanceOf1000() {
        BigDecimal balance = jdbcAccountDao.getBalance("user1");
        String actual = balance + "";
        String expected = "1000.00";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getBalanceRetrieves599() {
        BigDecimal balance = jdbcAccountDao.getBalance("user2");
        String actual = balance + "";
        String expected = "599.00";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void withdraw100FromUser2AndAccountDecreasesBy100() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(100.00);
        jdbcAccountDao.sendFunds(withdrawAmount, (long) 1011);
        BigDecimal balance = jdbcAccountDao.getBalance("user2");
        String actual = balance + "";
        String expected = "499.00";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void withdraw50FromUser1AndAccountDecreasesBy50() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(50.00);
        jdbcAccountDao.sendFunds(withdrawAmount, (long) 1010);
        BigDecimal balance = jdbcAccountDao.getBalance("user1");
        String actual = balance + "";
        String expected = "950.00";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void withdrawFailsIfAccountDoesNotHaveFunds() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(700.00);
        Assert.assertFalse(jdbcAccountDao.sendFunds(withdrawAmount, (long) 1011));
    }

    @Test
    public void accountGoesToZeroIfAllMoneyIsWithdrawn() {
        BigDecimal withdrawAmount = BigDecimal.valueOf(599.00);
        jdbcAccountDao.sendFunds(withdrawAmount, (long) 1011);
        BigDecimal balance = jdbcAccountDao.getBalance("user2");
        String actual = balance + "";
        String expected = "0.00";
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void Send400ToAccount2AndBalanceIncreasesBy400() {
        BigDecimal depositAmount = BigDecimal.valueOf(400.00);
        jdbcAccountDao.recieveFunds(depositAmount, (long) 1011);
        BigDecimal balance = jdbcAccountDao.getBalance("user2");
        String actual = balance + "";
        String expected = "999.00";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void Send400ToAccount1AndBalanceIncreasesBy400() {
        BigDecimal depositAmount = BigDecimal.valueOf(50.00);
        jdbcAccountDao.recieveFunds(depositAmount, (long) 1010);
        BigDecimal balance = jdbcAccountDao.getBalance("user1");
        String actual = balance + "";
        String expected = "1050.00";
        Assert.assertEquals(expected, actual);
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
