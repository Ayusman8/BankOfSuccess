package com.tg.bank_of_success.exception;

// Custom exception for account creation failure
@SuppressWarnings("serial")
public class AccountNotCreatedException extends Exception{
	public AccountNotCreatedException(String message) {
		super(message);
	}
}