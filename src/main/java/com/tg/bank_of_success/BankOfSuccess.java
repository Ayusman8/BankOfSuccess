package com.tg.bank_of_success;

import com.tg.bank_of_success.controller.AccountController;
import com.tg.bank_of_success.exception.AccountNotExistException;
import com.tg.bank_of_success.utilities.Constants;
import com.tg.bank_of_success.utilities.LogHandlerV1;

public class BankOfSuccess {

	private static final LogHandlerV1 log = new LogHandlerV1();

	public static void main(String[] args) {

		log.log(Constants.INFO, "Log Initiated - main method");
		System.out.println("Welcome to Bank Of Succcess");
		AccountController controller = new AccountController();
		try {
			controller.start();
			System.out.println("Thank you visit again!");
		} catch (AccountNotExistException e) {
			log.log(Constants.WARNING, "Main method " + "- start method - " + " " + e.getMessage());
		} catch (Exception e) {
			log.log(Constants.WARNING, "Main method " + "- start method - " + " " + e.getMessage());
		}
	}
}