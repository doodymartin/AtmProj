/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.datamodel;

import java.util.List;

/**
 * Class used to model the notes requested during a withdrawal.
 *
 */
public class WithdrawalCurrency {

    List<AtmEngineCurrency> atmEngineCurrencies;

    public List<AtmEngineCurrency> getAtmEngineCurrencies() {
        return atmEngineCurrencies;
    }

    public void setAtmEngineCurrencies(List<AtmEngineCurrency> atmEngineCurrencies) {
        this.atmEngineCurrencies = atmEngineCurrencies;
    }

}
