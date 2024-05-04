package com.tg.bank_of_success.external_service;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.entities.SavingsAccount;
import com.tg.bank_of_success.utilities.Constants;

public class SavingsImpl implements IAccount{
	
	@Override
	public boolean open(Account account) {
		
		SavingsAccount saving = (SavingsAccount) account;
		
		boolean isQualified = false;
		
		if(account.getAccountType().contains(Constants.SAVINGS)) {
			if(saving.getAge() >= 18) {
				isQualified = true;
			}
		}
		return isQualified;
	}

}
