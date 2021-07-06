package com.hargrove.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class CheckingAccount {
    private UUID id;
    private String accountNumber;
    private BigDecimal Balance;
    private UUID userID;
    private List<Transaction> transactions;
    // java.time.Instant represents date in UTC, or an offset of +/-0000.
    private Instant dateOfAccountCreation;

    // Default constructor needed for this error:
    // SEVERE [http-nio-8080-exec-6] org.eclipse.yasson.internal.Unmarshaller.deserializeItem Cannot create instance of a class: class com.hargrove.models.CheckingAccount, No default constructor found.
    public CheckingAccount() {}

    public CheckingAccount(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    // Setter required for JSON parsing to POJO.
    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return Balance;
    }
    public void setBalance(BigDecimal balance) {
        Balance = balance;
    }

    public UUID getUserID() {
        return userID;
    }
    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Instant getDateOfAccountCreation() {
        return dateOfAccountCreation;
    }
    public void setDateOfAccountCreation(Instant dateOfAccountCreation) {
        this.dateOfAccountCreation = dateOfAccountCreation;
    }
}
