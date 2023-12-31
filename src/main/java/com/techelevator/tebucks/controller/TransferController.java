package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController

public class TransferController {

    private TransferDao transferDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

    public TransferController(TransferDao dao) {
        this.transferDao = dao;
    }

    @RequestMapping(path = "/api/account/transfers", method = RequestMethod.GET)
    public List<Transfer> list(int userId) {
        if (userId != 0) {
        }
        return transferDao.getAllTransactionsByUserId(userId);
    }

    @RequestMapping(path = "/api/users", method = RequestMethod.GET)
    public List<User> list() {
        return userDao.findAll();
    }

    @RequestMapping(path = "/api/transfers/{id}", method = RequestMethod.GET)
    public Transfer get(@PathVariable int id) {
        return transferDao.getTransferByTransferId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/api/transfers", method = RequestMethod.POST)
    public Transfer createNewTransfer(@RequestBody NewTransferDto newTransferDto) {
        return transferDao.createTransfer(newTransferDto);
    }

    @RequestMapping(path = "/api/transfers/{id}/status", method = RequestMethod.PUT)
    public Transfer transferStatusUpdateDto(@Valid @RequestBody TransferStatusUpdateDto transferStatusUpdateDto, @PathVariable int id) {
        Transfer result = transferDao.updateTransfer(transferStatusUpdateDto);
        if (result != null) {
            return result;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
    }
//    @RequestMapping(path = "/api/account/balance", method = RequestMethod.GET)
//    public BigDecimal getAmountByBalance(int balance) throws Exception {
//
//        return dao.getAmountByBalance(getAmountByBalance(balance));
//    }
}
//	TransferStatusUpdateDto