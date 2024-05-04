package com.tg.bank_of_success.external_service;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.entities.CurrentAccount;
import com.tg.bank_of_success.entities.SavingsAccount;

public class AccountImplFactory {

    public static IAccount create(Account account){
        //Based on the choice return the implementation
    	if (account instanceof CurrentAccount) {
            return new CurrentImpl();
        } else if (account instanceof SavingsAccount) {
            return new SavingsImpl();
        } else {
            throw new IllegalArgumentException("Unsupported account type");
        }
    }
}
