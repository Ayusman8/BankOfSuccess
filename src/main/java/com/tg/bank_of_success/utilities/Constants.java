package com.tg.bank_of_success.utilities;

// Interface for storing constants used throughout the application
public abstract class Constants {
	
    // Logging levels
	public static final String WARNING = "WARNING";
	public static final String ERROR = "ERROR";
	public static final String INFO = "INFO";
	public static final String MESSAGE = "MESSAGE";
	public static final String SEVERE = "SEVERE";
	public static final String DEBUG = "DEBUG";
	public static final String INPUT = "INPUT";
	
	// Account types
	public static final String SAVINGS = "savings";
	public static final String CURRENT = "current";
	
	// Privilege levels
	public static final String SILVER = "SILV";
	public static final String GOLD = "GOL";
	public static final String PLATINUM = "PLAT";
	
	// Gender options
	public static final String MALE = "male";
	public static final String FEMALE = "female";
	
	// Transaction types
	public static final String DEBIT = "-";
	public static final String CREDIT = "+";
	
	// Log file paths
	public static final String EXECUTION_LOG = "ExecutionLog";
	public static final String TRANSACTION_LOG = "TransactionLog";
	public static final String DATE_AND_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String LOG_FILE_PATH = "O:\\LOGS\\BankOfSuccess.txt";
	public static final String TRANSACTION_LOG_FILE_PATH = "O:\\TRANSACTION_LOGS\\transactions.txt";
}
