package com.tg.bank_of_success.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.entities.CurrentAccount;
import com.tg.bank_of_success.entities.GENDER;
import com.tg.bank_of_success.entities.SavingsAccount;
import com.tg.bank_of_success.exception.AccountNotActiveException;
import com.tg.bank_of_success.exception.AccountNotExistException;
import com.tg.bank_of_success.exception.LowBalanceException;
import com.tg.bank_of_success.exception.TransactionLimitExceeded;
import com.tg.bank_of_success.exception.WrongPinNumberException;
import com.tg.bank_of_success.external_service.AccountFactory;
import com.tg.bank_of_success.manager.AccountManager;
import com.tg.bank_of_success.repository.AccountRepository;
import com.tg.bank_of_success.utilities.Constants;
import com.tg.bank_of_success.utilities.LogHandlerV1;

// Controller class for managing accounts and their operations
public class AccountController {

    // Log handler instance for logging events
    private static final LogHandlerV1 log = new LogHandlerV1();

    // Manager instance for managing accounts
    AccountManager manager = new AccountManager();
    AccountRepository accountRepository = new AccountRepository();
    Scanner sc = new Scanner(System.in);

    // Start the account management system
    public void start() throws AccountNotExistException {
        
        String accountNumber = null;
        int choice = 0;
        
        while (true) {
            // Display menu options
            System.out.println("Choose an option:\r\n" + "1. Open Account\r\n" + "2. Deposit Funds\r\n"
                    + "3. Withdraw Funds\r\n" + "4. Transfer Funds\r\n" + "5. Show account balance\r\n" + "6. Show account information\r\n" + "7. Close Account\r\n" +"8. Exit");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
            case 1:
                // Open new accounts
                Account account = null;
                account = open(account);
                accountRepository.addAccount(account);
                break;

            case 2:
                // Deposit funds into an account
                System.out.println("Enter your account number");
                accountNumber = sc.nextLine();
                
                if (accountRepository.getAccountByAccountNumber(accountNumber) != null) {
                    initiateDeposit(accountRepository.getAccountByAccountNumber(accountNumber));
                } else {
                    throw new AccountNotExistException("No account created yet");
                }
                break;

            case 3:
                // Withdraw funds from an account
                System.out.println("Enter your account number");
                accountNumber = sc.nextLine();
                
                if (accountRepository.getAccountByAccountNumber(accountNumber) != null) {
                    initiateTransaction(accountRepository.getAccountByAccountNumber(accountNumber));
                } else {
                    throw new AccountNotExistException("No account created yet");
                }
                break;

            case 4:
                System.out.println("Enter the account number of sender");
                String senderAccountNumber = sc.nextLine();
                
                System.out.println("Enter the account number of receiver");
                String receiverAccountNumber = sc.nextLine();
                
                // Transfer funds between accounts
                Account sender = accountRepository.getAccountByAccountNumber(senderAccountNumber);
                Account reciver = accountRepository.getAccountByAccountNumber(receiverAccountNumber);
                if (sender != null && reciver != null) {
                    initiateTransfer(sender, reciver);
                } else {
                    throw new AccountNotExistException("No account created yet");
                }
                break;
            
            case 5:
                // Close an account
                System.out.println("Enter your account number");
                accountNumber = sc.nextLine();
                System.out.println("Enter your pin number");
                String pinNumber = sc.nextLine();
                if (accountRepository.getAccountByAccountNumber(accountNumber) != null) {
                    try {
                        manager.showAccountBalance(accountRepository.getAccountByAccountNumber(accountNumber), pinNumber);
                    }catch(WrongPinNumberException e) {
                        log.log(Constants.WARNING, "AccountManager " + "- showAccountBalance method - " + " " + e.getMessage());
                    }
                } else {
                    throw new AccountNotExistException("No account created yet");
                }
                break;    
            
            case 6:
                // Close an account
                System.out.println("Enter your account number");
                accountNumber = sc.nextLine();
                if (accountRepository.getAccountByAccountNumber(accountNumber) != null) {
                    manager.showAccountInformation(accountRepository.getAccountByAccountNumber(accountNumber));
                    } else {
                    throw new AccountNotExistException("No account created yet");
                }
                break;
                
            case 7:
                // Close an account
                System.out.println("Enter your account number");
                accountNumber = sc.nextLine();
                if (accountRepository.getAccountByAccountNumber(accountNumber) != null) {
                    close(accountRepository.getAccountByAccountNumber(accountNumber));
                } else {
                    throw new AccountNotExistException("No account created yet");
                }
                break;

            case 8:
                System.out.println("Exiting Bank app...");
                return;

            default:
                log.log(Constants.ERROR, "Wrong Input! Try again");
                break;
            }
        }
    }

    // Open a new account
    public Account open(Account account) {

        String accountType = acceptUserChoice();
        account = createAccount(accountType);
        boolean status = manager.open(account);

        // Check if the account was successfully opened
        if (status) {
            log.log(Constants.INFO,
                    "Congratulations! " + account.getName()
                            + " your account has been created successfully with account number "
                            + account.getAccountNumber() + " and with user ID " + account.getId() + " on "
                            + account.getDate() + " and your account has been activated.");
        } else {
            log.log(Constants.INFO,
                    "Your request has been denied from manager please ensure you are above 18 and recheck your phone number.");
        }
        return account;
    }

    // Initiate a fund transfer between accounts
    private void initiateTransfer(Account sender, Account receiver) {

        System.out.println("Enter the amount to transfer");
        double amountToTransfered = Double.parseDouble(sc.nextLine());
        System.out.println("Enter your PIN number");
        int pinNumber = Integer.parseInt(sc.nextLine());
        if (receiver.getAccountNumber().contentEquals(receiver.getAccountNumber())) {
            try {
                if (manager.transferFunds(sender, receiver, amountToTransfered, pinNumber)) {
                    log.log(Constants.INFO, "Transaction successful! Rs " + amountToTransfered + " has been sent to "
                            + receiver.getName() + ". Updated Balance is Rs " + sender.getAccountBalance());
                }
            } catch (TransactionLimitExceeded e) {
                log.log(Constants.WARNING,
                        getClass().getSimpleName() + " " + "- initiateTransfer method - " + " " + e.getMessage());
                System.out.println("Try again!");
            } catch (LowBalanceException e) {
                log.log(Constants.WARNING,
                        getClass().getSimpleName() + " " + "- initiateTransfer method - " + " " + e.getMessage());
                System.out.println("Try again!");
            } catch (WrongPinNumberException e) {
                log.log(Constants.WARNING,
                        getClass().getSimpleName() + " " + "- initiateTransfer method - " + " " + e.getMessage());
                System.out.println("Try again!");
            } catch (AccountNotActiveException e) {
                log.log(Constants.WARNING,
                        getClass().getSimpleName() + " " + "- initiateTransfer method - " + " " + e.getMessage());
                System.out.println("Try again!");
            } catch (Exception e) {
                log.log(Constants.WARNING,
                        getClass().getSimpleName() + " " + "- initiateTransfer method - " + " " + e.getMessage());
                System.out.println("Try again!");
            }
        } else {
            log.log(Constants.INFO, "There is no account associated with account number " + receiver.getAccountNumber()
                    + " Please try again!");
        }
    }

    // Initiate a deposit into an account
    private void initiateDeposit(Account account) {

        System.out.println("Enter the amount to deposit");
        double amount = Double.parseDouble(sc.nextLine());
        System.out.println("Enter the PIN of your account");
        int pin = Integer.parseInt(sc.nextLine());
        try {
            if (manager.isDeposited(account, amount, pin)) {
                log.log(Constants.INFO, "Deposit Completed. Updated balance is " + account.getAccountBalance());
            }
        } catch (LowBalanceException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateDeposit method - " + " " + e.getMessage());
            System.out.println("Try again!");
        } catch (WrongPinNumberException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateDeposit method - " + " " + e.getMessage());
            System.out.println("Try again!");
        } catch (AccountNotActiveException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateDeposit method - " + " " + e.getMessage());
            System.out.println("Try again!");
        } catch (Exception e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateDeposit method - " + " " + e.getMessage());
            System.out.println("Try again!");
        }
    }

    // Initiate a transaction (withdrawal) from an account
    private void initiateTransaction(Account account) {

        System.out.println("Enter the amount to withdraw");
        double amount = Double.parseDouble(sc.nextLine());
        System.out.println("Enter the PIN of your account");
        int pin = Integer.parseInt(sc.nextLine());
        try {
            if (manager.isWithdrawn(account, amount, pin)) {
                log.log(Constants.INFO, "Transaction Completed. Updated balance is " + account.getAccountBalance());
            }
        } catch (LowBalanceException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateTransaction method - " + " " + e.getMessage());
            System.out.println("Try again!");
        } catch (WrongPinNumberException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateTransaction method - " + " " + e.getMessage());
            System.out.println("Try again!");
        } catch (AccountNotActiveException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateTransaction method - " + " " + e.getMessage());
            System.out.println("Try again!");
        } catch (Exception e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- initiateTransaction method - " + " " + e.getMessage());
            System.out.println("Try again!");
        }
    }

    // Accept the user's choice of account type
    private String acceptUserChoice() {

        System.out.println("What type of account you want to open(Savings/Current)");
        String choice = sc.nextLine();
        choice = choice.toLowerCase();
        return choice;
    }

    // Create an account based on the account type should be implemented in account
    // factory
    private Account createAccount(String accountType) {

        Account account = AccountFactory.create(accountType);
        getCommonAccountInformarion(account);
        if (accountType.contains(Constants.SAVINGS)) {
            savingsAccountinformation(account);
        } else {
            currentAccountInformation(account);
        }
        return account;
    }

    // Open a savings account
    private void savingsAccountinformation(Account account) {

        SavingsAccount savings = (SavingsAccount) account;
        int choice = 0;

        try {
            account.setAccountType(Constants.SAVINGS);
            System.out.println("Enter date of birth in (dd-MM-yyyy) format");
            String dob = sc.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
            Date date = sdf.parse(dob);
            savings.setDateOfBirth(date);
            savings.setAge(calculateAge(date));
        } catch (ParseException e) {
            log.log(Constants.WARNING,
                    getClass().getSimpleName() + " " + "- openSavingsAccount method - " + " " + e.getMessage());
        }
        System.out.println("Enter your gender\n1. MALE\n2. FEMALE\n3. TRANSGENDER");
        choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
        case 1:
            savings.setGender(GENDER.MALE);
            break;
        case 2:
            savings.setGender(GENDER.FEMALE);
            break;
        case 3:
            savings.setGender(GENDER.TRANSGENDER);
            break;
            
        default:
            System.out.println("Wrong input!");
            break;
        }
        System.out.println("Enter your Phone number");
        savings.setPhoneNumber(Long.parseLong(sc.nextLine()));
    }

    // Open a current account
    private void currentAccountInformation(Account account) {

        account.setAccountType(Constants.CURRENT);
        CurrentAccount current = (CurrentAccount) account;
        System.out.println("Enter your company's name");
        current.setCompanyName(sc.nextLine());
        System.out.println("Enter your website");
        current.setUserWebsite(sc.nextLine());
        System.out.println("Enter your registration number");
        current.setRegistrationNumber(Long.parseLong(sc.nextLine()));
    }

    // Calculate age based on date of birth
    private int calculateAge(Date dob) {

        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(dob);
        Calendar currentCalendar = Calendar.getInstance();
        return currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
    }

    // Get the current date
    private String currentDate() {

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateTime = dateFormat.format(currentDate);
        return currentDateTime;
    }

    // Close an account
    private void close(Account account) {

        if (manager.close(account)) {
            account.setAccountStatus(false);
            account.setAccountClosingDate(currentDate());
            log.log(Constants.INFO,
                    "Account number " + account.getAccountNumber() + " has been closed on " + currentDate());
        }
    }

    // Create PIN for an account
    private void createPin(Account account) {

        String userPin = account.getPinCode();
        if (userPin == null) {
            while (true) {
                System.out.println("Please enter a PIN of 4 digits for your account");
                String pin = sc.nextLine();
                System.out.println("Please re-enter the PIN you just entered");
                String confirmPin = sc.nextLine();
                if (pin.equals(confirmPin) && pin.length() == 4) {
                    account.setPinCode(pin);
                    System.out.println("PIN has been set");
                    break;
                } else {
                    log.log(Constants.ERROR, "PIN mismatched, Please try again");
                }
            }
        }
    }

    // Get common account information
    private Account getCommonAccountInformarion(Account account) {

        int choice = 0;

        System.out.println("Enter your first name");
        String firstName = sc.nextLine();
        System.out.println("Enter your second name");
        String secondName = sc.nextLine();
        account.setName(firstName + " " + secondName);
        System.out.println("Please enter the initial amount you want to deposit");
        account.setDeposit(Double.parseDouble(sc.nextLine()));
        System.out.println("Please select the privilege for your account");
        System.out.println("1. SILVER \n2. GOLD \n3. PLATINUM");
        choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
        case 1:
            account.setPrivilage(Constants.SILVER);
            break;
        case 2:
            account.setPrivilage(Constants.GOLD);
            break;
        case 3:
            account.setPrivilage(Constants.PLATINUM);
            break;
        default:
            System.out.println("Wrong Input!");
            break;
        }
        if (account.getPrivilege().contains(Constants.SILVER)) {
            account.setLimit(25000);
        } else if (account.getPrivilege().contains(Constants.GOLD)) {
            account.setLimit(50000);
        } else if (account.getPrivilege().contains(Constants.PLATINUM)) {
            account.setLimit(100000);
        }
        createPin(account);
        return account;
    }
}
