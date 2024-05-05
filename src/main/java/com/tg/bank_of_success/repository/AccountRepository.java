package com.tg.bank_of_success.repository;

import java.util.ArrayList;
import java.util.List;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.exception.AccountNotExistException;

public class AccountRepository {

    // List to store accounts
    private List<Account> accounts;
    
    // Constructor to initialize the list
    public AccountRepository() {
        // Initialize the list
        this.accounts = new ArrayList<Account>();
    }
        
    // Method to add an account to the list
    public void addAccount(Account account) {
        accounts.add(account);
    }
    
    // Method to get an account by account number
    public Account getAccountByAccountNumber(String accountNumber) throws AccountNotExistException {
        // Iterate through the list of accounts
        for(Account account : accounts) {
            // Check if the account number matches the specified account number
            if(account.getAccountNumber().equals(accountNumber.trim())) {
                // If a match is found, return the account
                return account;
            }
        }
        // If no account is found with the specified account number, throw an exception
        throw new AccountNotExistException("No account found with account number " + accountNumber);
    }
}
