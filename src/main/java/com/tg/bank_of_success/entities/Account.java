package com.tg.bank_of_success.entities;

import com.tg.bank_of_success.utilities.Constants;

//Class that represents an account of an account holder in the bank
//Common information across accounts is defined in the class

public abstract class Account {
	
	private String accountType;
	private double availableLimit;
	private long id;
	private String accountNumber;
	private String name;
	private String pinCode;
	private double accountBalance;
	private boolean isActive;
	private String privilege;
	private String accountCreationDate;
	private String accountClosingDate;

	// Getters and Setters
	public void setId(long id) {

		this.id = id;
	}

	public Long getId() {

		return this.id;
	}

	public void setAccountNumber(String accountNumber) {

		this.accountNumber = accountNumber;
	}

	public String getAccountNumber() {

		return this.accountNumber;
	}

	public void setDate(String accountCreationDate) {

		this.accountCreationDate = accountCreationDate;
	}

	public String getDate() {

		return this.accountCreationDate;
	}

	public void setPinCode(String pinCode) {

		this.pinCode = pinCode;
	}

	public String getPinCode() {

		return this.pinCode;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getName() {

		return this.name;
	}

	public void setPrivilage(String privilege) {

		if (extractPrivilege(privilege) != null) {
			this.privilege = extractPrivilege(privilege);
		} else {
			System.out.println("Invalid Selection!");
		}
	}

	public String getPrivilege() {

		return this.privilege;
	}

	public void setAccountStatus(boolean isActive) {

		this.isActive = isActive;
	}

	public void setDeposit(double depositAmount) {

		this.accountBalance = depositAmount;
	}

	public double getAccountBalance() {

		return this.accountBalance;
	}

	public boolean getAccountStatus() {

		return this.isActive;
	}

	public double getAvailableLimit() {

		return availableLimit;
	}

	public void setLimit(double limit) {

		availableLimit = limit;
	}

	public boolean isOverLimit(double amount) {

		return availableLimit < amount;
	}

	public String getAccountClosingDate() {

		return accountClosingDate;
	}

	public void setAccountClosingDate(String accountClosingDate) {

		this.accountClosingDate = accountClosingDate;
	}

	public String getAccountType() {

		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String extractPrivilege(String privilege) {

		if (privilege.contains(Constants.SILVER)) {
			return PRIVILEGE.SILVER.toString();
		} else if (privilege.contains(Constants.GOLD)) {
			return PRIVILEGE.GOLD.toString();
		} else if (privilege.contains(Constants.PLATINUM)) {
			return PRIVILEGE.PLATINUM.toString();
		} else {
			return null;
		}
	}
}
