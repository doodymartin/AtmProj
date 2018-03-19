/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.datamodel;

import com.jjProj.atmEngine.common.AtmEngineRequestType;

/**
 * Class used to model the HTTP REST requests that the ATM engine handles.
 *
 * This class handles a superset of all possible request parameters for the REST requests entering the ATM engine.
 *
 */
public class AtmEngineRequest {
    String accountNo;
    String accountPin;
    AtmEngineRequestType atmEngineRequestType;
    int withdrawalAmount;

    public AtmEngineRequest(AtmEngineRequestType atmEngineRequestType, String accountNo, String accountPin) {
        this.atmEngineRequestType = atmEngineRequestType;
        this.accountNo = accountNo;
        this.accountPin = accountPin;
        this.withdrawalAmount = 0;
    }
    public AtmEngineRequest(AtmEngineRequestType atmEngineRequestType, String accountNo, String accountPin, int withdrawalAmount) {
        this.atmEngineRequestType = atmEngineRequestType;
        this.accountNo = accountNo;
        this.accountPin = accountPin;
        this.withdrawalAmount = withdrawalAmount;
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
    public AtmEngineRequestType getAtmEngineRequestType() {
        return atmEngineRequestType;
    }
    public void setAtmEngineRequestType(AtmEngineRequestType atmEngineRequestType) {
        this.atmEngineRequestType = atmEngineRequestType;
    }
    public int getWithdrawalAmount() {
        return withdrawalAmount;
    }
    public void setWithdrawalAmount(int withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

}
