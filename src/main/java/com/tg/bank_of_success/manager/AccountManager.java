package com.tg.bank_of_success.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.exception.AccountNotActiveException;
import com.tg.bank_of_success.exception.InvalidAmountFoundException;
import com.tg.bank_of_success.exception.InvalidPinNumberFoundException;
import com.tg.bank_of_success.exception.LowBalanceException;
import com.tg.bank_of_success.exception.NoAccountFoundException;
import com.tg.bank_of_success.exception.TransactionLimitExceeded;
import com.tg.bank_of_success.exception.WrongAccountNumberFoundException;
import com.tg.bank_of_success.exception.WrongPinNumberException;
import com.tg.bank_of_success.external_service.AccountImplFactory;
import com.tg.bank_of_success.external_service.IAccount;
import com.tg.bank_of_success.utilities.Constants;
import com.tg.bank_of_success.utilities.LogHandlerV1;
import com.tg.bank_of_success.utilities.TransactionLog;

public class AccountManager {
    
    // Initialize LogHandlerV1 and TransactionLog
    private static final LogHandlerV1 LOG = new LogHandlerV1();
    private TransactionLog transactionLog = new TransactionLog();
    
    // Method to open an account
    public boolean open(Account account) {
    	
        // Create an instance of IAccountImpl based on the account type
        IAccount accImpl = AccountImplFactory.create(account);
        // Call the open method of the corresponding account implementation
        boolean isQualified = accImpl.open(account);
        
        // If account is qualified, generate ID, account number, and set status and date
        if(isQualified) {
        	
            generateId(account);
            generateAccountNumber(account);
            account.setAccountStatus(isQualified);
            String currentDateTime = currentDate();
            account.setDate(currentDateTime);
        }
        return isQualified;
    }
    
    // Method to generate ID for the account
    public void generateId(Account account) {
    	
        Random random = new Random();
        account.setId(random.nextLong(10000, 99999));
    }
    
    // Method to generate account number for the account
    public void generateAccountNumber(Account account) {
    	
        Random random = new Random();
        long accountNumber = 1000;
        long randomNum = random.nextLong() % 1000000L;
        randomNum = Math.abs(randomNum);
        account.setAccountNumber(String.valueOf(accountNumber)+ String.valueOf(randomNum));
    }
    
    // Method to withdraw funds from an account
    public boolean isWithdrawn(Account account, String accountNumber, double amount, int pinNumber) throws TransactionLimitExceeded, AccountNotActiveException, InvalidPinNumberFoundException, InvalidAmountFoundException, WrongPinNumberException, LowBalanceException, WrongAccountNumberFoundException, NoAccountFoundException {
        
        boolean isWithdrawn = false;
        if(account != null) {
            if(account.getAccountStatus()) {
                if(account.getAccountNumber().contains(accountNumber)) {
                    if(amount >= 1 && amount!=0) {
                        if(pinNumber>0) {
                            if(account.getAccountBalance()>= amount) {
                                if(!account.isOverLimit(amount)) {
                                    if(Integer.parseInt(account.getPinCode())== pinNumber) {
                                        account.setDeposit(account.getAccountBalance()-amount);
                                        transactionLog.limitUpdater(account, amount*-1);
                                        isWithdrawn = true;
                                        LOG.logTransaction(Constants.DEBIT, String.valueOf(amount), account);
                                    }else {
                                        throw new WrongPinNumberException("Sorry! It seems like your entred wrong PIN");    
                                    }
                                }else {
                                    throw new TransactionLimitExceeded("Your current transaction limit is Rs."+account.getAvailableLimit()+" . Please try again!");
                                }
                            }else {
                                throw new LowBalanceException("Sorry! It seems like your account has not sufficient balance.");    
                            }
                        }else {
                            throw new InvalidPinNumberFoundException("Invalid amount! Please try again");
                        }
                    }else {
                        throw new InvalidAmountFoundException("Invalid amount! Please try again");
                    }
                }else {
                    throw new WrongAccountNumberFoundException("Sorry! It seems like you entred wrong account number");    
                }
            }else {
                throw new AccountNotActiveException("Sorry! It seems like your account has been deactivated, Please contact the branch manager.");    
            }
        }else {
            throw new NoAccountFoundException("Account not found! Please ensure account is created");
        }
        return isWithdrawn;
    }
    
    // Method to transfer funds between accounts
    public boolean transferFunds(Account sender, Account receiver, double amountToTransfer, int pinNumber) throws AccountNotActiveException, WrongPinNumberException, LowBalanceException, TransactionLimitExceeded{
        //Declaration
        boolean isTransferred = false;
        //1. Is from account active
        if(sender.getAccountStatus()) {
            //2. Is to account active
            if(receiver.getAccountStatus()) {
                //3. Check if pin number is valid
                if(Integer.parseInt(sender.getPinCode())== pinNumber) {
                    //4. Check if sufficient funds are available in from account
                    if(sender.getAccountBalance()>= amountToTransfer) {
                        //5. Check if the transfer limit is exceeded
                        if(!sender.isOverLimit(amountToTransfer)) {
                            //6. Deduct amount from sender 
                            sender.setDeposit(sender.getAccountBalance()-amountToTransfer);
                            //7. Add amount to receiver
                            receiver.setDeposit(receiver.getAccountBalance()+amountToTransfer);
                            //8. Add into Log the transfer made
                            transactionLog.limitUpdater(sender, amountToTransfer*-1);
                            //9. Update the status of transfer
                            isTransferred = true;
                            LOG.logTransaction(Constants.DEBIT, String.valueOf(amountToTransfer), sender);
                            LOG.logTransaction(Constants.CREDIT, String.valueOf(amountToTransfer), receiver);

                        }else {
                            throw new TransactionLimitExceeded("Your current transaction limit is Rs."+sender.getAvailableLimit()+" . Please try again!");
                        }
                    }else {
                        throw new LowBalanceException("Sorry! It seems like your account has not sufficient balance.");    
                    }
                }else {
                    throw new WrongPinNumberException("Sorry! It seems like your entred wrong PIN");    
                }
            }else {
                throw new AccountNotActiveException("Sorry! It seems like receiver account has been deactivated transfer cannot be done.");    
            }
        }else {
            throw new AccountNotActiveException("Sorry! It seems like your account has been deactivated, Please contact the branch manager.");    
        }
        //10. Return the status
        return isTransferred;
    }
    
    // Method to deposit funds into an account
    public boolean isDeposited(Account account, String accountNumber, double amount, int pinNumber) throws WrongPinNumberException, LowBalanceException, WrongAccountNumberFoundException, AccountNotActiveException {
        boolean isDeposited = false;
        if(account.getAccountStatus()) {
            if(account.getAccountNumber().contains(accountNumber)) {
                if(Integer.parseInt(account.getPinCode())== pinNumber) {
                    account.setDeposit(account.getAccountBalance()+ amount);
                    transactionLog.limitUpdater(account, amount);
                    isDeposited = true;
                    LOG.logTransaction(Constants.CREDIT, String.valueOf(amount), account);
                }else {
                    throw new WrongPinNumberException("Sorry! It seems like your entred wrong PIN");    
                }
            }else {
                throw new WrongAccountNumberFoundException("Sorry! It seems like you entred wrong account number");    
            }
        }else {
            throw new AccountNotActiveException("Sorry! It seems like your account has been deactivated, Please contact the branch manager.");    
        }
        return isDeposited;
    }
    
    // Method to get current date and time
    protected static String currentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        String currentDateTime = dateFormat.format(currentDate);
        return currentDateTime;
    }
    
    // Method to close an account
    public boolean close(Account account) {
        if(account.getAccountStatus()) {
            return true;
        }else {
            LOG.log(Constants.INFO,"Account is already deactivated");
            return false;
        }
    }
}
