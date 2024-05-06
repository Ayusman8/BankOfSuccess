package com.tg.bank_of_success.external_service;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.entities.CurrentAccount;
import com.tg.bank_of_success.entities.SavingsAccount;
import com.tg.bank_of_success.utilities.Constants;

public class AccountFactory {
	
    public static Account create(String accountType){
        //Here will create either savings or current account object and return 
       if(accountType.equals(Constants.SAVINGS)) {
    	   return new SavingsAccount();
       }else {
    	   return new CurrentAccount();
       }
    }
}
