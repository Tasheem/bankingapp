package com.hargrove.services;

import com.hargrove.dao.CheckingAccountDAO;
import com.hargrove.enums.TransactionCategory;
import com.hargrove.models.CheckingAccount;
import com.hargrove.models.Transaction;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CheckingAccountService {
    private CheckingAccountDAO dao;

    public CheckingAccount getAccount(UUID id) {
        dao = new CheckingAccountDAO();
        CheckingAccount account = dao.queryChecking(id);

        return account;
    }

    public CheckingAccount getAccount(String accountNumber) {
        dao = new CheckingAccountDAO();
        CheckingAccount account = dao.queryChecking(accountNumber);

        return account;
    }

    public List<CheckingAccount> getAllAccounts() {
        dao = new CheckingAccountDAO();
        List<CheckingAccount> accounts = dao.queryAllChecking();

        return accounts;
    }

    public CheckingAccount create(UUID userID, BigDecimal deposit) throws Exception {
        dao = new CheckingAccountDAO();

        CheckingAccount checking = new CheckingAccount(UUID.randomUUID());
        // UUID userID = UUID.fromString(uuid);
        checking.setUserID(userID);

        Instant date = Instant.now();
        checking.setDateOfAccountCreation(date);

        checking.setBalance(deposit);

        int floor = 1000;
        int bound = 10000;
        int random = ThreadLocalRandom.current().nextInt(floor, bound);

        String accountNumPrefix = userID.toString().substring(0, 9);
        String accountNumSuffix = String.valueOf(random);
        String accountNumber = accountNumPrefix + accountNumSuffix;
        System.out.println("Account Number: " + accountNumber);
        checking.setAccountNumber(accountNumber);

        boolean successful = dao.saveChecking(checking);
        if(!successful) {
            throw new Exception("Account was not successfully saved.");
        }

        return checking;
    }

    public void updateBalance(Transaction transaction) {
        dao = new CheckingAccountDAO();

        if(transaction.getCategory() == TransactionCategory.SEND) {
            BigDecimal receivingAmount = transaction.getAmount();
            BigDecimal sendingAmount = receivingAmount.negate();
            dao.updateBalance(transaction.getOriginAccountNumber(), sendingAmount);

            dao.updateBalance(transaction.getDestinationAccountNumber(), receivingAmount);
        }
        else if(transaction.getCategory() == TransactionCategory.WITHDRAWAL) {
            BigDecimal withdrawal = transaction.getAmount().negate();
            dao.updateBalance(transaction.getOriginAccountNumber(), withdrawal);
        }
        else
            dao.updateBalance(transaction.getOriginAccountNumber(), transaction.getAmount());
    }

    public boolean deleteChecking(UUID userID) {
        dao = new CheckingAccountDAO();

        boolean isSuccessful = dao.deleteAccount(userID.toString());

        return isSuccessful;
    }
}
