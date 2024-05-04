package com.tg.bank_of_success.exception;

// Custom exception for wrong PIN numbers
@SuppressWarnings("serial")
public class WrongPinNumberException extends Exception{
	public WrongPinNumberException(String message) {
		super(message);
	}
}