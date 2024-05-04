package com.tg.bank_of_success.exception;

// Custom exception for invalid amounts
@SuppressWarnings("serial")
public class InvalidAmountFoundException extends Exception{
	public InvalidAmountFoundException(String message) {
		super(message);
	}
}