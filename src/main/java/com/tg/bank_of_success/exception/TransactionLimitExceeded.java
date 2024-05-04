package com.tg.bank_of_success.exception;

// Custom exception for exceeding transaction limits
@SuppressWarnings("serial")
public class TransactionLimitExceeded extends Exception{
	public TransactionLimitExceeded(String message) {
		super(message);
	}
}