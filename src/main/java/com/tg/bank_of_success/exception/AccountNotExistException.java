package com.tg.bank_of_success.exception;

// Custom exception for account creation failure
@SuppressWarnings("serial")
public class AccountNotExistException extends Exception{
	public AccountNotExistException(String message) {
		super(message);
	}
}