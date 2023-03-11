package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController


public class AccountController {

    private AccountDao dao;

    public AccountController(AccountDao dao){
        this.dao = dao;
    }


}
//GET	/api/account/transfers	N/A	List<Transfer>