package com.hargrove.dao;

import com.hargrove.models.Transaction;
import com.hargrove.utilities.JDBCUtil;
import java.sql.PreparedStatement;

public class TransactionDAO {
    private JDBCUtil jdbc;

    public void saveTransaction(Transaction transaction) {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        String insert = "INSERT INTO transactions Values (?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement ps = jdbc.getConnection().prepareStatement(insert);

            ps.setString(1, transaction.getId().toString());
            ps.setString(2, transaction.getDescription());
            ps.setString(3, transaction.getDate().toString());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setString(5, transaction.getCategory().name());
            ps.setString(6, transaction.getOriginAccountNumber());
            ps.setString(7, transaction.getDestinationAccountNumber());

            int affectedRows = ps.executeUpdate();
            System.out.println("Affected Rows: " + affectedRows);

            ps.close();
        } catch (Exception e) {
            System.out.println("Error Creating Checking Account in TransactionDAO");
            System.out.println(e);
        }

        jdbc.closeConnection();
    }
}
