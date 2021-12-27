package com.hargrove.dao;

import com.hargrove.enums.TransactionCategory;
import com.hargrove.models.Transaction;
import com.hargrove.utilities.JDBCUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionDAO {
    private JDBCUtil jdbc;

    public List<Transaction> queryTransactions(String accountNumber) {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        String query = String.format("SELECT * FROM transactions WHERE origin_account = '%s'", accountNumber);
        List<Transaction> result = new ArrayList<>();

        Statement st = null;
        try {
            st = jdbc.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                /*
                 * DB table columns:
                 * id, description, date, amount, type, origin_account, destination_account
                 */
                Transaction transaction = new Transaction();

                UUID id = UUID.fromString(rs.getString(1));
                transaction.setId(id);

                transaction.setDescription(rs.getString(2));

                Instant creationDate = Instant.parse(rs.getString(3));
                transaction.setDate(creationDate);

                transaction.setAmount(rs.getBigDecimal(4));
                transaction.setCategory(TransactionCategory.valueOf(rs.getString(5)));
                transaction.setOriginAccountNumber(rs.getString(6));
                transaction.setDestinationAccountNumber(rs.getString(7));

                result.add(transaction);
            }

            st.close();
        } catch (SQLException e) {
            System.out.println("Error Getting Transactions In TransactionDAO");
            System.out.println(e);
        }

        jdbc.closeConnection();

        return result;
    }

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
            System.out.println("Error Creating Transaction In TransactionDAO");
            System.out.println(e);
        }

        jdbc.closeConnection();
    }
}
