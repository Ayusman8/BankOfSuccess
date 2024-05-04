package com.tg.bank_of_success.exception;

// Custom exception for inactive accounts

@SuppressWarnings("serial")
public class AccountNotActiveException extends Exception{
	public AccountNotActiveException(String message) {
		super(message);
	}
}
