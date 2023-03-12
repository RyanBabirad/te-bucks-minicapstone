package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.NewTransferDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TransferStatusUpdateDto;
import com.techelevator.tebucks.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.techelevator.tebucks.model.Transfer.*;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcUserDao jdbcUserDao;

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
        Transfer transfer = new Transfer();

        String sql = "INSERT INTO transfer (transfer_type, transfer_status, user_from, user_to, amount)" +
                "VALUES (?, 'Approved', ?, ? , ?) RETURNING transfer_id;";
        Integer results = jdbcTemplate.queryForObject(sql, Integer.class, newTransferDto.getTransferType(),
                newTransferDto.getUserFrom(), newTransferDto.getUserTo(), newTransferDto.getAmount()); //newTransferDto.getTransferStatus()

        transfer.setTransferId(results);
        transfer.setTransferType(newTransferDto.getTransferType());
        transfer.setUserTo(jdbcUserDao.getUserById(newTransferDto.getUserTo()));
        transfer.setUserFrom(jdbcUserDao.getUserById(newTransferDto.getUserFrom()));
        transfer.setAmount(newTransferDto.getAmount());

        if (newTransferDto.getTransferType().equalsIgnoreCase(TRANSFER_TYPE_SEND)) {
            transfer.setTransferStatus(TRANSFER_STATUS_APPROVED);

            String sql2 = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql2, newTransferDto.getAmount(), newTransferDto.getUserFrom());

            String sql3 = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            jdbcTemplate.update(sql3, newTransferDto.getAmount(), newTransferDto.getUserTo());
        }

        else if (newTransferDto.getTransferType().equalsIgnoreCase(TRANSFER_TYPE_REQUEST) && newTransferDto.getTransferStatus().equalsIgnoreCase(TRANSFER_STATUS_APPROVED)) {
            transfer.setTransferStatus(TRANSFER_STATUS_PENDING);

            String sql2 = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql2, newTransferDto.getAmount(), newTransferDto.getUserFrom());

            String sql3 = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            jdbcTemplate.update(sql3, newTransferDto.getAmount(), newTransferDto.getUserTo());
        }

        else if (newTransferDto.getTransferType().equalsIgnoreCase(TRANSFER_TYPE_REQUEST) && newTransferDto.getTransferStatus().equalsIgnoreCase(TRANSFER_STATUS_REJECTED)) {
            transfer.setTransferStatus(TRANSFER_STATUS_PENDING);

            String sql2 = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql2, newTransferDto.getAmount(), newTransferDto.getUserFrom());

            String sql3 = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            jdbcTemplate.update(sql3, newTransferDto.getAmount(), newTransferDto.getUserTo());
        }

        else if (newTransferDto.getTransferType().equalsIgnoreCase(TRANSFER_TYPE_REQUEST)) {
            transfer.setTransferStatus(TRANSFER_STATUS_PENDING);

            String sql2 = "UPDATE account SET balance = balance - ? WHERE user_id = ?;";
            jdbcTemplate.update(sql2, newTransferDto.getAmount(), newTransferDto.getUserFrom());

            String sql3 = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
            jdbcTemplate.update(sql3, newTransferDto.getAmount(), newTransferDto.getUserTo());
        }

        return transfer;

    }

//    @Override
//    public Transfer createTransfer(NewTransferDto newTransferDto) {
//        Transfer transfer = new Transfer();
//
//        String sql = "INSERT INTO transfer (transfer_type, transfer_status, user_from, user_to, amount)" +
//                "VALUES (?, 'Approved', ?, ? , ?) RETURNING transfer_id;";
//        int id;
//        SqlRowSet resultOfInsert = jdbcTemplate.queryForRowSet(sql, newTransferDto.getTransferType(),
//                newTransferDto.getUserFrom(), newTransferDto.getUserTo(), newTransferDto.getAmount()); //newTransferDto.getTransferStatus()
//        if (resultOfInsert.next()) {
//            transfer = mapRowToTransfer(resultOfInsert);
//        }
//        transfer.setTransferId(id);
//        return transfer;
//    }

    @Override
    public Transfer updateTransfer(TransferStatusUpdateDto transferStatusUpdateDto) {

        String sql = "UPDATE transfer " +
                "SET transfer_status = ? " +
                "WHERE transfer_id = ?;";

        jdbcTemplate.update(sql, transferStatusUpdateDto.getTransferStatus());

        return null;
    }

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
