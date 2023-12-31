package com.techelevator.tebucks.dao;


import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Account account = mapRowToAccount(results);
            allAccounts.add(account);
        }

        return allAccounts;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        }
        return null;
    }

//    @Override
//    public BigDecimal getBalance(int accountId) throws Exception {
//        String sql = "SELECT account_id, user_id, balance " +
//                "FROM account " +
//                "WHERE account_id = ?;";
//
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
//        if (results.next()) {
//            return mapRowToAccount(results).getBalance();
//        }
//        throw new Exception("Could not find Account");
//    }

    @Override
    public Boolean createAccount(int userId) {
        String sql = "INSERT INTO account (user_id) " +
                "VALUES (?) " +
                "RETURNING account_id;";

        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        return newId != null;
    }
    // check
// -----> username ---> user id ---> account Id ----> balance
//    @Override
//    public Account getBalanceByUserId(int userId) throws Exception {
//        String sql = "SELECT * FROM account " +
//                "WHERE user_id = ?;";
//
//        Account results = jdbcTemplate.queryForObject(sql, Account.class);
//        if(results != null){
//         return results;
//     }
//        throw new Exception("Could not find Account");
//    }

    @Override
    public Account getBalanceByUserId(int userId) {
        Account account = null;
        String sql = "SELECT account_id, balance, user_id FROM account " +
                "WHERE user_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
            account = mapRowToAccount(results);
        }
        //throw new Exception("Could not find Account");
        return account;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUserId(rs.getInt("user_id"));

        return account;
    }
}
