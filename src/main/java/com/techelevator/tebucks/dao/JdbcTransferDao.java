package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;
import com.techelevator.tebucks.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getSentTransactionsByUserId(int userId) {
        List<Transfer> sentTransferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "WHERE user_from = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            sentTransferList.add(transfer);
        }

        return sentTransferList;
    }

    @Override
    public List<Transfer> getReceivedTransactionsByUserId(int userId) {
        List<Transfer> receivedTransferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "WHERE user_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            receivedTransferList.add(transfer);
        }
        return receivedTransferList;
    }

    @Override
    public List<Transfer> getAllTransactionsByUserId(int userId) {
        List<Transfer> allTransferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "WHERE user_to = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            allTransferList.add(transfer);
        }
        return allTransferList;
    }

    @Override
    public Transfer createTransfer(NewTransferDto newTransferDto) {
        String sql = "INSERT INTO transfer " +
                "VALUES (?, ?, ?, ? , ?) " +
                "RETURNING transfer_id;";

        SqlRowSet resultOfInsert = jdbcTemplate.queryForRowSet(sql, newTransferDto.getTransferType(),
                    newTransferDto.getTransferStatus(), newTransferDto.getUserFrom(), newTransferDto.getUserTo(), newTransferDto.getAmount());
        return null;
    }

    @Override
    public Transfer updateTransfer(TransferStatusUpdateDto transferStatusUpdateDto) {
    String sql = "UPDATE transfer " +
            "SET transfer_status = ? " +
            "WHERE transfer_id = ?;";

    jdbcTemplate.update(sql,transferStatusUpdateDto.getTransferStatus());
        return null;
    }

//    @Override
//    public Transfer updateTransfer(Transfer transfer) {
//        String sql = "UPDATE transfer " +
//                "SET transfer_type = ?, transfer_status = ?, user_from = ?, user_to = ?, amount = ? " +
//                "WHERE transfer_id = ?;";
//
//        jdbcTemplate.update(sql, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getUserFrom(),
//                transfer.getUserTo(), transfer.getAmount(), transfer.getTransferId());
//        return null;
//    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT * FROM transfer " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferType(rs.getString("transfer_type"));
        transfer.setTransferStatus(rs.getString("transfer_status"));
        transfer.setUserFrom(rs.getObject("user_from", User.class));
        transfer.setUserTo(rs.getObject("user_to", User.class));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
