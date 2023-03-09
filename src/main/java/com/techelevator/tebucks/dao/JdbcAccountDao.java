package com.techelevator.tebucks.dao;


import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.model.Account;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    @Override
    public BigDecimal getBalance(int accountId) {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            return mapRowToAccount(results).getBalance();
        }
        return BigDecimal.valueOf(0); /////ask if value of 0 is gooooodddddd
    }

    @Override
    public Account createAccount(Account account) {
        String sql = "INSERT INTO account " +
                "VALUES (?, ?) " +
                "RETURNING account_id;";

        SqlRowSet resultOfInsert = jdbcTemplate.queryForRowSet(sql, account.getUserId(), (account.getBalance()));

        if (resultOfInsert.next()) {
            int newAccountId = resultOfInsert.getInt("account_id");
            account.setAccountId(newAccountId);
            account.setBalance(BigDecimal.valueOf(1000));
            return account;
        }
        return null;
    }

    @Override
    public void updateAccount(Account account) {
        String sql = "UPDATE account " +
                "SET user_id = ?, balance = ? " +
                "WHERE account_id = ?;";

        jdbcTemplate.update(sql, account.getUserId(), account.getBalance(), account.getAccountId());
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
