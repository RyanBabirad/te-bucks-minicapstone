package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.util.List;

public interface TransferDao {


    List<Transfer> getSentTransactionsByUserId(int userId);//possibly (int id)

    List<Transfer> getReceivedTransactionsByUserId(int userId);

    Transfer createTransfer(Transfer transfer);

    void updateTransfer(Transfer transfer);

    Transfer getTransferByTransferId(int transferId);


}
