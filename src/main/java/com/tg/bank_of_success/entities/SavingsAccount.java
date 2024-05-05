package com.tg.bank_of_success.entities;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

// SavingsAccount class representing a savings account, extends the Account class
public class SavingsAccount extends Account {
	private Date dateOfBirth; // Date of birth of the account holder
    private GENDER gender; // Gender of the account holder
    private String phoneNumber; // Phone number of the account holder
    private int age; // Age of the account holder
	private String name;

    //Getters and Setters
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setGender(GENDER gender) {
        this.gender = gender;
    }

    public GENDER getGender() {
        return this.gender;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber.toString();
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    // Calculates the age of the account holder based on the date of birth
    public int calculateAge(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now(); 
        Period period = Period.between(dateOfBirth, currentDate); 
        return period.getYears(); 
    }
}
