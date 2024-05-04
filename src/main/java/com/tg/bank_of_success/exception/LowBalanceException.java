package com.tg.bank_of_success.exception;

// Custom exception for low balance
@SuppressWarnings("serial")
public class LowBalanceException extends Exception{
	public LowBalanceException(String message) {
		super(message);
	}
}