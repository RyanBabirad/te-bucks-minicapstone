package com.techelevator.tebucks.model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // TODO add withdraw and deposit methods
}
