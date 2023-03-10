package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.TransferDao;
import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping ("/api/transfers")
public class TransferController {

    private TransferDao dao;

    public TransferController(TransferDao dao){
        this.dao = dao;
    }

   // @RequestMapping(path = "/api/account/transfers" ,method = RequestMethod.GET)
   //     public List<Transfer> list(int userId){
   //     if(userId != 0){
    //    }return dao.getAllTransactionsByUserId(userId);
   // }


    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
        public Transfer get(@PathVariable int id){
        return dao.getTransferByTransferId(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
        public Transfer createNewTransfer(@RequestBody NewTransferDto newTransferDto){
        return dao.createTransfer(newTransferDto);
    }


    @RequestMapping(path = "/{id}/status", method = RequestMethod.PUT)
        public Transfer transferStatusUpdateDto(@Valid @RequestBody TransferStatusUpdateDto transferStatusUpdateDto, @PathVariable int id){
        Transfer result = dao.updateTransfer(transferStatusUpdateDto);
        if(result != null){
            return result;
        }throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
    }
}
//	TransferStatusUpdateDto