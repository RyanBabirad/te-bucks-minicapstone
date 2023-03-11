package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    List<Transfer> getSentTransactionsByUserId(int userId);//possibly (int id)

    List<Transfer> getReceivedTransactionsByUserId(int userId);

    List<Transfer> getAllTransactionsByUserId(int userId);

    Transfer createTransfer(NewTransferDto newTransferDto);

    Transfer updateTransfer(TransferStatusUpdateDto transferStatusUpdateDto);

    Transfer getTransferByTransferId(int transferId);



}
