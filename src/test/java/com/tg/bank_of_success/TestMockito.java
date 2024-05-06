package com.tg.bank_of_success;

import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.tg.bank_of_success.entities.Account;
import com.tg.bank_of_success.exception.AccountNotActiveException;
import com.tg.bank_of_success.exception.InvalidAmountFoundException;
import com.tg.bank_of_success.exception.InvalidPinNumberFoundException;
import com.tg.bank_of_success.exception.LowBalanceException;
import com.tg.bank_of_success.exception.TransactionLimitExceeded;
import com.tg.bank_of_success.exception.WrongPinNumberException;
import com.tg.bank_of_success.manager.AccountManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TestMockito {
    // Mock dependencies
    Account account = Mockito.mock(Account.class);
    // Initialize AccountManager with mocked dependencies
    AccountManager manager = new AccountManager();
    JSONArray testCasesArray;
    JSONObject testCaseObject;

    @Before
    public void setUp() {
        // Define behavior of mocked account
        getTestJSONArray();
        when(account.getAccountNumber()).thenReturn("1000109021");
        when(account.getPinCode()).thenReturn("1234");
        when(account.getAccountStatus()).thenReturn(true);
        when(account.getAccountBalance()).thenReturn(5000.0);
    }

    // Method to read test cases from JSON file
    public void getTestJSONArray() {
        JSONTokener tokener = null;
        try {
            // Change the file path according to your file location
            tokener = new JSONTokener(new FileReader("Test_Input//Test Case.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject(tokener);
        this.testCasesArray = jsonObject.getJSONArray("test_cases");
    }

    // Method to set the current test object
    public void setTestObject(String test) {
        for (int i = 0; i < testCasesArray.length(); i++) {
            this.testCaseObject = testCasesArray.getJSONObject(i);
            if (testCaseObject.getString("name").contains(test))
                break;
        }
    }
    
    // Test case for withdrawing with sufficient balance
    @Test
    public void testWithdrawForSufficientBalance() {
        setTestObject("happy");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));
        double initialBalance = account.getAccountBalance();
        boolean isWithdrawn = false;
        double expectedResult = account.getAccountBalance() - amount;
        
        when(account.getAccountBalance()).thenReturn(5000.0);

        try {
            isWithdrawn = manager.isWithdrawn(account, amount, pinNumber);
        } catch(Exception e) {
            e.printStackTrace();
        }
        assertTrue(isWithdrawn);
        assertEquals(expectedResult, initialBalance-amount, 0);
    }
    
    // Test case for withdrawing with insufficient balance
    @Test
    public void testWithdrawForInsufficientBalance() {
        setTestObject("lowBalance");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));

        assertThrows(LowBalanceException.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }

    // Test case for withdrawing with wrong pin
    @Test
    public void testWithdrawForWrongPin() {
        setTestObject("wrongPin");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));

        assertThrows(WrongPinNumberException.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }

    // Test case for withdrawing from inactive account
    @Test
    public void testWithdrawForInactiveAccount() {
        setTestObject("inactiveAccount");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));
        boolean accountStatus = input.getBoolean("accountStatus");

        when(account.getAccountStatus()).thenReturn(accountStatus);
        
        assertThrows(AccountNotActiveException.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }

    // Test case for withdrawing zero amount
    @Test
    public void testWithdrawForZeroAmount() {
        setTestObject("zeroAmount");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));

        assertThrows(InvalidAmountFoundException.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }
    
    // Test case for withdrawing with negative amount
    @Test
    public void testWithdrawForNegativeAmount() {
        setTestObject("negativeAmount");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));

        assertThrows(InvalidAmountFoundException.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }

    // Test case for withdrawing with negative pin number
    @Test
    public void testWithdrawForNegativePin() {
        setTestObject("negativePin");

        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));
        boolean accountStatus = input.getBoolean("accountStatus");

        when(account.getAccountStatus()).thenReturn(accountStatus);

        assertThrows(InvalidPinNumberFoundException.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }

    // Test case for withdrawing with large amount exceeding the transaction limit
    @Test
    public void testWithdrawForLargeAmount() {
        setTestObject("largeAmount");
        
        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));        
        boolean accountStatus = input.getBoolean("accountStatus");
        double accountBalance = Double.parseDouble(input.getString("accountBalance"));
        double availablelimit = Double.parseDouble(input.getString("availableLimit"));
        
        when(account.getAccountStatus()).thenReturn(accountStatus);
        when(account.getAccountBalance()).thenReturn(accountBalance);
        when(account.isOverLimit(amount)).thenReturn(availablelimit<amount);
        
        assertThrows(TransactionLimitExceeded.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }
    
    // Test case for withdrawing with silver privilege exceeding the transaction limit
    @Test
    public void testWithdrawForSilverprivilege() {
        setTestObject("silver");
        
        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));        
        boolean accountStatus = input.getBoolean("accountStatus");
        double accountBalance = Double.parseDouble(input.getString("accountBalance"));
        double availablelimit = Double.parseDouble(input.getString("availableLimit"));
        
        when(account.getAccountStatus()).thenReturn(accountStatus);
        when(account.getAccountBalance()).thenReturn(accountBalance);
        when(account.isOverLimit(amount)).thenReturn(availablelimit<amount);
        
        assertThrows(TransactionLimitExceeded.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }
    
    // Test case for withdrawing with gold privilege exceeding the transaction limit
    @Test
    public void testWithdrawForGoldprivilege() {
        setTestObject("gold");
        
        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));        
        boolean accountStatus = input.getBoolean("accountStatus");
        double accountBalance = Double.parseDouble(input.getString("accountBalance"));
        double availablelimit = Double.parseDouble(input.getString("availableLimit"));
        
        when(account.getAccountStatus()).thenReturn(accountStatus);
        when(account.getAccountBalance()).thenReturn(accountBalance);
        when(account.isOverLimit(amount)).thenReturn(availablelimit<amount);
        
        assertThrows(TransactionLimitExceeded.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }
    
    // Test case for withdrawing with platinum privilege exceeding the transaction limit
    @Test
    public void testWithdrawForPlatinumprivilege() {
        setTestObject("platinum");
        
        JSONObject input = testCaseObject.getJSONObject("input");
        
        double amount = Double.parseDouble(input.getString("amount"));
        int pinNumber = Integer.parseInt(input.getString("pinNumber"));        
        boolean accountStatus = input.getBoolean("accountStatus");
        double accountBalance = Double.parseDouble(input.getString("accountBalance"));
        double availablelimit = Double.parseDouble(input.getString("availableLimit"));
        
        when(account.getAccountStatus()).thenReturn(accountStatus);
        when(account.getAccountBalance()).thenReturn(accountBalance);
        when(account.isOverLimit(amount)).thenReturn(availablelimit<amount);
        
        assertThrows(TransactionLimitExceeded.class, () -> {
            manager.isWithdrawn(account, amount, pinNumber);
        });
    }
}
