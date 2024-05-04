package com.tg.bank_of_success.external_service;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.entities.CurrentAccount;
import com.tg.bank_of_success.utilities.Constants;

public class CurrentImpl implements IAccount{

	@Override
	public boolean open(Account account) {
		
		boolean isQualified = false;
		
		if(account.getAccountType().contains(Constants.CURRENT)) {
			CurrentAccount current = (CurrentAccount) account;
			if(current.getRegistrationNumber() != null) {
				isQualified = true;
			}
		}
		return isQualified;
	}

}
