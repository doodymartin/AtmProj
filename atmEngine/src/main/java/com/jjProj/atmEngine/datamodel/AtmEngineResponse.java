/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.datamodel;

/**
 * Class used to model the HTTP REST responses that the ATM engine returns to the user.
 *
 * This class holds a superset of all possible response parameters for all REST services.
 * This is the class that will be transformed to a JSON object.
 */
public class AtmEngineResponse {

    private int balance;
    private int responseCode;
    private String responseMessage;
    private WithdrawalCurrency withdrawalCurrency;

    public AtmEngineResponse(int balance, int responseCode, String responseMessage) {
        this.balance = balance;
        this.responseMessage = responseMessage;
        this.responseCode = responseCode;
    }

    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public WithdrawalCurrency getWithdrawalCurrency() {
        return withdrawalCurrency;
    }

    public void setWithdrawalCurrency(WithdrawalCurrency withdrawalCurrency) {
        this.withdrawalCurrency = withdrawalCurrency;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
