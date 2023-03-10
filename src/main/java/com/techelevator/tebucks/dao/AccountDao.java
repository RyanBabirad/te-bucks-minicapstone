package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {


    List<Account> findAllAccounts();

    Account getAccountByAccountId(int accountId);

    Account getAccountByUserId(int userId);

    BigDecimal getBalance(int accountId) throws Exception;

    Boolean createAccount(int userId);

}
