package com.tg.bank_of_success.exception;

// Custom exception for no account found
@SuppressWarnings("serial")
public class NoAccountFoundException extends Exception{
	public NoAccountFoundException(String message) {
		super(message);
	}
}