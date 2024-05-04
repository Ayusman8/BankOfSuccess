package com.tg.bank_of_success.exception;

// Custom exception for invalid PIN numbers
@SuppressWarnings("serial")
public class InvalidPinNumberFoundException extends Exception{
	public InvalidPinNumberFoundException(String message) {
		super(message);
	}
}