package com.hargrove.dao;

import com.hargrove.models.CheckingAccount;
import com.hargrove.utilities.JDBCUtil;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CheckingAccountDAO {
    JDBCUtil jdbc;

    private CheckingAccount getChecking(String query) {
        CheckingAccount account = null;

        try {
            /*
             * Database user table columns:
             *
             * id, account_number, creation_date, user_id, balance
             */
            jdbc = new JDBCUtil();
            jdbc.establishConnection();
            Statement st = jdbc.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();

            UUID id = UUID.fromString(rs.getString(1));
            account = new CheckingAccount(id);
            account.setAccountNumber(rs.getString(2));

            Instant creationDate = Instant.parse(rs.getString(3));
            account.setDateOfAccountCreation(creationDate);

            UUID userID = UUID.fromString(rs.getString(4));
            account.setUserID(userID);

            account.setBalance(rs.getBigDecimal(5));

            st.close();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            jdbc.closeConnection();
        }

        return account;
    }
    public CheckingAccount queryChecking(String accountNumber) {
        String query = String.format("SELECT * FROM checking_accounts WHERE account_number = \"%s\"", accountNumber);
        CheckingAccount account = getChecking(query);

        return account;
    }

    public CheckingAccount queryChecking(UUID id) {
        String query = String.format("SELECT * FROM checking_accounts WHERE user_id = \"%s\"", id.toString());
        CheckingAccount account = getChecking(query);

        return account;
    }

    public List<CheckingAccount> queryAllChecking() {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        String query = "SELECT * FROM checking_accounts;";

        List<CheckingAccount> accounts = new ArrayList<>();
        try {
            Statement st = jdbc.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                /*
                 * Database user table columns:
                 *
                 * id, account_number, creation_date, user_id, balance
                 */
                UUID id = UUID.fromString(rs.getString(1));
                CheckingAccount checking = new CheckingAccount(id);

                checking.setAccountNumber(rs.getString(2));

                Instant creationDate = Instant.parse(rs.getString(3));
                checking.setDateOfAccountCreation(creationDate);

                UUID userID = UUID.fromString(rs.getString(4));
                checking.setUserID(userID);

                checking.setBalance(rs.getBigDecimal(5));
                accounts.add(checking);

                st.close();
            }
        } catch (SQLException sqlException) {
            System.out.println("Exception in CheckingAccountDAO");
            System.out.println(sqlException);
        }

        jdbc.closeConnection();
        return accounts;
    }

    public boolean saveChecking(CheckingAccount checking) {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        boolean successful = true;

        String insert = String.format("INSERT INTO checking_accounts Values (?, ?, ?, ?, ?);");

        try {
            PreparedStatement ps = jdbc.getConnection().prepareStatement(insert);

            ps.setString(1, checking.getId().toString());
            ps.setString(2, checking.getAccountNumber());
            ps.setString(3, checking.getDateOfAccountCreation().toString());
            ps.setString(4, checking.getUserID().toString());
            ps.setBigDecimal(5, checking.getBalance());

            int affectedRows = ps.executeUpdate();
            System.out.println("Affected Rows: " + affectedRows);

            ps.close();
        } catch (Exception e) {
            System.out.println("Error Creating Checking Account in CheckingAccountDAO");
            System.out.println(e);
            successful = false;
        }

        jdbc.closeConnection();
        return successful;
    }

    private BigDecimal getBalance(String accountNumber) {
        String query = String.format("SELECT balance FROM checking_accounts WHERE account_number " +
                "= \"%s\";", accountNumber);

        try {
            Statement st = jdbc.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.next();
            BigDecimal balance = rs.getBigDecimal(1);

            st.close();
            return balance;
        } catch(SQLException e) {
            System.out.println("CheckingAccountDAO: Error in getBalance method.");
            System.out.println(e);
            return null;
        }
    }

    public int updateBalance(String accountNumber, BigDecimal amount) {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        BigDecimal balance = getBalance(accountNumber);
        if(balance == null)
            return -1;
        BigDecimal updatedBalance = balance.add(amount);

        String update = "UPDATE checking_accounts SET balance = ?" +
                " WHERE account_number = ?";

        try {
            PreparedStatement ps = jdbc.getConnection().prepareStatement(update);

            System.out.println("Old Balance: " + balance);
            System.out.println("Updated Balance: " + updatedBalance);
            ps.setBigDecimal(1, updatedBalance);
            ps.setString(2, accountNumber);

            int affectedRows = ps.executeUpdate();
            System.out.println("Affected Rows: " + affectedRows);

            ps.close();
        } catch(SQLException e) {
            System.out.println("CheckingAccountDAO: Error in updateBalance method.");
            System.out.println(e);
            return -1;
        } finally {
            jdbc.closeConnection();
        }

        return 0;
    }

    public boolean deleteAccount(String userID) {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        String delete = String.format("DELETE FROM checking_accounts WHERE user_id = \"%s\"", userID);

        boolean isSuccessful = false;
        try {
            Statement st = jdbc.getConnection().createStatement();
            int affectedRows = st.executeUpdate(delete);
            System.out.println("Rows affected: " + affectedRows);
            isSuccessful = true;
        } catch (SQLException e) {
            System.out.println("CheckingAccountDAO: Exception in deleteAccount method.");
            System.out.println(e);
        } finally {
            jdbc.closeConnection();
        }

        return isSuccessful;
    }
}
