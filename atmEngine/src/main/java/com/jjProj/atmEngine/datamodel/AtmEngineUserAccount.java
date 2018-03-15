/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.datamodel;

/**
 * Class used to model the User Accounts held in the ATM engine.
 *
 */
public class AtmEngineUserAccount {

    String accountNo;
    String accountPin;
    int balance;
    int overdraft;

    public AtmEngineUserAccount(String accountNo, String accountPin, int balance, int overdraft) {
        this.accountNo = accountNo;
        this.accountPin = accountPin;
        this.balance = balance;
        this.overdraft = overdraft;
    }

    public String getAccountNo() {
        return accountNo;
    }
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }
    public String getAccountPin() {
        return accountPin;
    }
    public void setAccountPin(String accountPin) {
        this.accountPin = accountPin;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public int getOverdraft() {
        return overdraft;
    }
    public void setOverdraft(int overdraft) {
        this.overdraft = overdraft;
    }



}
