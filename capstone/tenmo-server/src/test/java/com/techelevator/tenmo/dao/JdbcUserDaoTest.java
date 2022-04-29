//package com.techelevator.tenmo.dao;
//
//import com.techelevator.tenmo.model.User;
//import net.bytebuddy.matcher.FilterableList;
//import org.junit.*;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.SingleConnectionDataSource;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class JdbcUserDaoTest {
//
//    private static SingleConnectionDataSource dataSource;
//    private JdbcUserDao jdbcUserDao;
//
////    @BeforeClass
////    public static void setup(){
////        dataSource = new SingleConnectionDataSource();
////        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
////        dataSource.setUsername("postgres");
////        dataSource.setPassword("postgres1");
////        dataSource.setAutoCommit(false);
////    }
////
////    @Before
////    public void setupData() {
////        //Create test users
////        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
////        String userOneSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
////                "VALUES (1010, 'user1', '$2a$10$E3L5F3XzWQdn/qeN5zgZg.mc3.wZBjfGoGNHbT8OfXB3K03n6U/py')";
////        jdbcTemplate.update(userOneSql);
////        String userTwoSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
////                "VALUES (1011, 'user2', '$2a$10$5JXQ7khJSFUJ6SV3DlZlHO4KRtcP6y6WvaFu7Tf18PJMKyEpTSZJ6')";
////        jdbcTemplate.update(userTwoSql);
////        String userThreeSql = "INSERT INTO tenmo_user (user_id, username, password_hash) " +
////                "VALUES (1012, 'user3', 'HASH')";
////        jdbcTemplate.update(userThreeSql);
////        jdbcUserDao = new JdbcUserDao(dataSource);
////    }
//
//    @Test
//    public void findAllUsersReturnsAListThatDoesNotContainCurrentUser() {
//        List<User> users = new ArrayList<>();
//        users = jdbcUserDao.findAll("user1");
//        int expected = 5;
//        Assert.assertEquals(expected, users.size());
//    }
//
//    @After
//    public void rollback() throws SQLException {
//        dataSource.getConnection().rollback();
//    }
//
//    @AfterClass
//    public static void closeDataSource(){
//        dataSource.destroy();
//    }
//
//}
