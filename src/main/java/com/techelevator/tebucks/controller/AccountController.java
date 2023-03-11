package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {

    private AccountDao accountDao;
    @Autowired
    private UserDao userDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "api/account/balance", method = RequestMethod.GET)
    public Account getBalanceByUserId(Principal principal) { //throws Exception {
        int userId = userDao.findIdByUsername(principal.getName());
        //return dao.getAccountByUserId(userId);
//    if(userId != 0){
//
        return accountDao.getBalanceByUserId(userId);
//    }
////    }
//
//    throw new Exception("UserId not Valid");

    }
//
//    public int getFromPrincipal(Principal principal) {
//        int userId = userDao.findIdByUsername(principal.getName());
//        return userId;
//    }
}
//GET	/api/account/balance	N/A	Account

//protected String deposit(
// @RequestParam("accountNo") int accountNo,
// @RequestParam("amount") double amount,

