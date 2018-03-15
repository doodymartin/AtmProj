/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.service;


import com.jjProj.atmEngine.datamodel.AtmEngineConfig;
import com.jjProj.atmEngine.datamodel.AtmEngineRequest;
import com.jjProj.atmEngine.datamodel.AtmEngineResponse;

/**
 * ATM Engine Service interface Class.
 */
public interface AtmEngineService {
    public AtmEngineConfig initAtmEngine();
    public AtmEngineResponse getAccountBalance(AtmEngineRequest atmEngineRequest);
    public AtmEngineResponse makeAccountWithdrawal(AtmEngineRequest atmEngineRequest);

}
