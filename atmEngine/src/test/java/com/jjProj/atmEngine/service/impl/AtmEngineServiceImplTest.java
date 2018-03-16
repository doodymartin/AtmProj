/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.service.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import com.jjProj.atmEngine.common.AtmEngineError;
import com.jjProj.atmEngine.common.AtmEngineRequestType;
import com.jjProj.atmEngine.datamodel.AtmEngineConfig;
import com.jjProj.atmEngine.datamodel.AtmEngineRequest;
import com.jjProj.atmEngine.datamodel.AtmEngineResponse;
import com.jjProj.atmEngine.main.AtmEngine;


/**
 * Test class for testing the Service methods for the ATM Engine
 *
 * TODO : There is scope for more detailed edge test cases around account limits.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = AtmEngine.class)
public class AtmEngineServiceImplTest {

    @Autowired
    private AtmEngineServiceImpl atmEngineService;

    String accountNo = "123456789";
    String accountPin = "1234";

    String account2No = "987654321";
    String account2Pin = "4321";

    @Before
    public void setup() {
        assertNotNull(atmEngineService);
        testInitAtmEngine();
    }


    public void testInitAtmEngine() {
        /**
         * Test the ATM Engine init method
         */
        AtmEngineConfig atmEngineConfig = atmEngineService.initAtmEngine();
        /**
         * TODO : perform a more complete test some of the engine config created
         * but for the moment just test some key aspects of the config.
         */
        assertNotNull(atmEngineConfig);
        assertEquals(atmEngineConfig.getAtmEngineCurrencyNotes().size(), 4);
        assertEquals(atmEngineConfig.getAtmEngineUserAccounts().size(), 2);
        assertEquals(atmEngineConfig.getAtmMachineCurrentBalance(), 2000);

        atmEngineService.setAtmEngineConfig(atmEngineConfig);
    }

    @Test
    public void testGetAccountBalance() {
        /**
         * Test the get balance method
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        AtmEngineResponse atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 800);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }

    @Test
    public void testGetAccountBalanceForSecondUser() {
        /**
         * Test the get balance method
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,account2No,account2Pin);
        assertNotNull(atmEngineRequest);
        AtmEngineResponse atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 1230);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }


    @Test
    public void testGetMaximumAllowedWithdrawal() {
        /**
         * Test the get Maximum allowed withdrawal
         * i.e. Balance + overdraft should be the Maximum allowed withdrawal
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        AtmEngineResponse atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 1000);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }
    @Test
    public void testGetMaximumAllowedWithdrawalForSecondUser() {
        /**
         * Test the get Maximum allowed withdrawal
         * i.e. Balance + overdraft should be the Maximum allowed withdrawal
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,account2No,account2Pin);
        assertNotNull(atmEngineRequest);
        AtmEngineResponse atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 1380);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }

    @Test
    public void testMakeAccountWithdrawal() {
        /**
         * Test making an account withdrawal
         *
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,200);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 600); // test is the new balance
        assertNotNull(atmEngineResponse.getWithdrawalCurrency());
        assertNotNull(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies());
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().size(),1); // we should have four 50 notes so 1 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getDemonination(),50); // the only entry should be a 50 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentNumberOfNotes(),4); // four 50 notes
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentAmount(),200); // 200 should be the amount
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with a get balance it should be 600
         */
        AtmEngineRequest atmEngineRequest2 = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest2);
        AtmEngineResponse atmEngineResponse2 = atmEngineService.getAccountBalance(atmEngineRequest2);
        assertNotNull(atmEngineResponse2);
        assertEquals(atmEngineResponse2.getBalance(), 600);
        assertNull(atmEngineResponse2.getWithdrawalCurrency());
        assertEquals(atmEngineResponse2.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse2.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }

    @Test
    public void testInvalidWithdrawalAmount() {
        /**
         * Test making an account withdrawal of -2
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,-2);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.WITHDRAWAL_AMOUNT_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.WITHDRAWAL_AMOUNT_ERR.getErrorCode());
    }

    @Test
    public void testInvalidUserWithdrawal() {
        /**
         * Test making a withdrawal with an invalid accountNo
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,"123456788",accountPin, 10);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_NUMBER_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_NUMBER_ERR.getErrorCode());
    }

}