/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.common;

/**
 * ATM Engine Response Code enum.
 */
public enum AtmEngineError {
    REQUEST_SUCCESS(0,"Request Succeeded."),
    ACCOUNT_LOGIN_ERR(1,"User details incorrect."),
    ACCOUNT_PIN_ERR(2,"User Pin is incorrect."),
    ACCOUNT_NUMBER_ERR(3,"User account number incorrect."),
    ATM_AMOUNT_ERR(4,"ATM can only despense amount in multiples of 5."),
    ATM_FUNDS_ERR(5,"ATM does not have the funds for withdrawl."),
    ACCOUNT_FUNDS_ERR(6,"Insufficent funds in account."),
    WITHDRAWAL_AMOUNT_ERR(7,"The withdrawal amount is not valid."),
    UNKNOWN(7,"Unknown error.");

    private final int errorCode;
    private final String responseDescription;

    private AtmEngineError(int errorCode, String responseDescription) {
      this.errorCode = errorCode;
      this.responseDescription = responseDescription;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

}
