package com.tg.bank_of_success.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.tg.bank_of_success.entities.Account;

// Class to handle logging operations with version 1
public class LogHandlerV1{

	// Method to log messages
	public void log(String logType, String message) {
		String logContent = "["+currentDateAndTime()+"] "+logType+": "+message;
		// Print log messages to console based on type
		if(logContent.contains(Constants.INFO) || logContent.contains(Constants.MESSAGE)) {
			System.out.println(message);
		}else if(logContent.contains(Constants.WARNING) || logContent.contains(Constants.ERROR) || logContent.contains(Constants.SEVERE)) {
			System.err.println(logContent);
		}
		// Write log to file
		writeLog(Constants.EXECUTION_LOG, logContent);
	}
	
	// Method to log transaction details
	public void logTransaction(String transactionType, String amount , Account account) {
		String logContent = currentDateAndTime()+" ["+transactionType+amount+"] ["+account.getName()+"] ["+account.getAccountNumber()+"]";
		// Write transaction log to file
		writeLog(Constants.TRANSACTION_LOG, logContent);
	}

	// Method to write log to file
	private void writeLog(String logType, String logContent) {
		
		try {
			BufferedWriter writer = null;
			// Determine log file type and path
			if(logType.contains(Constants.TRANSACTION_LOG)) {
				writer = new BufferedWriter(new FileWriter(Constants.TRANSACTION_LOG_FILE_PATH, true));
			}else if(logType.contains(Constants.EXECUTION_LOG)){
				writer = new BufferedWriter(new FileWriter(Constants.LOG_FILE_PATH, true));
			}
			// Write log content to file
			writer.write(logContent);
			writer.newLine();
			writer.flush();
			writer.close();//in finally block
		} catch (IOException e) {
			// Handle IO exception
			e.printStackTrace();
		}
	}
	
	// Method to get current date and time
	private String currentDateAndTime() {
		LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_AND_TIME_FORMAT);
        String currentDateAndTime = currentTime.format(formatter);
        return currentDateAndTime;
	}
}
