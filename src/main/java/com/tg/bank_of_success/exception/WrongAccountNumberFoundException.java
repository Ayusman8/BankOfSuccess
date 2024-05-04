package com.tg.bank_of_success.exception;

// Custom exception for wrong account numbers
@SuppressWarnings("serial")
public class WrongAccountNumberFoundException extends Exception{
	public WrongAccountNumberFoundException(String message) {
		super(message);
	}
}
