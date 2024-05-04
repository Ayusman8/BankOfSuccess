package com.tg.bank_of_success.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import com.tg.bank_of_success.entities.Account;

public class TransactionLog {
    // HashMap to store account number as key and list of transaction amounts as value
    HashMap<Long, ArrayList<Double>> log = new HashMap<>();
    // ArrayList to store transaction amounts
    ArrayList<Double> list = new ArrayList<>();

    // Method to update the available limit and add transaction to log
    public void limitUpdater(Account account, double amount) {
        // Check if the available limit is sufficient for the transaction
        if (account.getAvailableLimit() >= amount) {
            // Update the available limit
            account.setLimit(account.getAvailableLimit() - amount);
            // Add the transaction to the log
            addTransfer(account, amount);
        } else {
            // Handle insufficient limit (not implemented in this example)
        }
    }

    // Method to add a transfer to the log
    public void addTransfer(Account account, double amount) {
        // Add the transaction amount to the list
        list.add(amount);
        // Add the list to the log
        addLog(account, list);
    }

    // Method to add a log entry to the log HashMap
    private void addLog(Account account, ArrayList<Double> list) {
        // Add the list of transaction amounts to the log HashMap with the account number as key
        log.put(Long.parseLong(account.getAccountNumber()), list);
    }
}
