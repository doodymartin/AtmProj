/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.datamodel;

import java.util.List;
import java.util.Map;

/**
 * ATM Engine Config Class, used to hold the following
 *
 * 1) Current amount available in the ATM
 * 2) The list of user accounts known by the ATM
 * 3) The Denomination and amount of notes held in the ATM
 */
public class AtmEngineConfig {

    int atmMachineCurrentBalance;
    List<AtmEngineUserAccount> atmEngineUserAccounts;
    Map<Integer, AtmEngineCurrency> atmEngineCurrencyNotes;


    public int getAtmMachineCurrentBalance() {
        return atmMachineCurrentBalance;
    }

    public void setAtmMachineCurrentBalance(int atmMachineCurrentBalance) {
        this.atmMachineCurrentBalance = atmMachineCurrentBalance;
    }

    public List<AtmEngineUserAccount> getAtmEngineUserAccounts() {
        return atmEngineUserAccounts;
    }

    public void setAtmEngineUserAccounts(
            List<AtmEngineUserAccount> atmEngineUserAccounts) {
        this.atmEngineUserAccounts = atmEngineUserAccounts;
    }

    public Map<Integer, AtmEngineCurrency> getAtmEngineCurrencyNotes() {
        return atmEngineCurrencyNotes;
    }

    public void setAtmEngineCurrencyNotes(
            Map<Integer, AtmEngineCurrency> atmEngineCurrencyNotes) {
        this.atmEngineCurrencyNotes = atmEngineCurrencyNotes;
    }

}
