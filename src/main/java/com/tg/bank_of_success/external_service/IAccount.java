package com.tg.bank_of_success.external_service;

import com.tg.bank_of_success.entities.Account;

public interface IAccount {
	//Implementation of open method
    boolean open(Account account);
}
