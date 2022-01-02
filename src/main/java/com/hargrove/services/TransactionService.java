package com.hargrove.services;

import com.hargrove.dao.TransactionDAO;
import com.hargrove.enums.TransactionCategory;
import com.hargrove.models.CheckingAccount;
import com.hargrove.models.Transaction;
import com.hargrove.models.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class TransactionService {
    TransactionDAO dao;
    CheckingAccountService checkingService;
    UserService userService;

    public List<Transaction> get(String userID) {
        checkingService = new CheckingAccountService();
        CheckingAccount checkingAccount = checkingService.getAccount(UUID.fromString(userID));

        dao = new TransactionDAO();
        List<Transaction> transactions = dao.queryTransactions(checkingAccount.getAccountNumber());

        return  transactions;
    }

    public void create(Transaction transaction) {
        // originAccountNumber == null.  Get from database.
        // Needed fields: id, date, description.
        dao = new TransactionDAO();
        checkingService = new CheckingAccountService();

        UUID newID = UUID.randomUUID();
        transaction.setId(newID);

        Instant date = Instant.now();
        transaction.setDate(date);

        if(transaction.getCategory() == TransactionCategory.SEND) {
            userService = new UserService();
            CheckingAccount receiverAccount = checkingService.getAccount(transaction.getDestinationAccountNumber());
            User receiver = userService.getUser(receiverAccount.getUserID());

            String description = String.format("$%s sent to %s %s", transaction.getAmount().toString(),
                    receiver.getFirstName(), receiver.getLastName());
            transaction.setDescription(description);
        }
        else if(transaction.getCategory() == TransactionCategory.WITHDRAWAL) {
            String description = "Withdrew $" + transaction.getAmount();
            transaction.setDescription(description);
        }
        else {
            String description = "Deposited $" + transaction.getAmount();
            transaction.setDescription(description);
        }

        dao.saveTransaction(transaction);
        checkingService.updateBalance(transaction);
    }
}
