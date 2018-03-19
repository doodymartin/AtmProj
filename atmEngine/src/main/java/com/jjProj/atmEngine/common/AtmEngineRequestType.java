/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.common;

/**
 * ATM Engine interface API enum.
 */
public enum AtmEngineRequestType {

    ATM_WITHDRAWAL("Withdrawal"),
    ATM_BALANCE("Balance"),
    ATM_MAXIMUM_WITHDRAWAL_BALANCE("Max Withdrawal Balance"),
    UNKNOWN("");

    private String identifier;

    AtmEngineRequestType(String identifier) {
        this.identifier = identifier;
    }

    public String identifier() {
        return identifier;
    }

}
