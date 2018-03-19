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
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 600);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }



    @Test
    public void testMakeAccountWithdrawalToMaxUserBalance() {
        /**
         * Test making an account withdrawal to the Max user balance
         *
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,800);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // test is the new balance
        assertNotNull(atmEngineResponse.getWithdrawalCurrency());
        assertNotNull(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies());
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().size(),1); // we should have four 50 notes so 1 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getDemonination(),50); // the only entry should be a 50 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentNumberOfNotes(),16); // four 50 notes
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentAmount(),800); // 800 should be the amount
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with a get balance it should be 0
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with the Max user account balance it should be 200
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 200);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }


    @Test
    public void testMakeAccountWithdrawalToMaxUserBalanceIncludingOverdraft() {
        /**
         * Test making an account withdrawal to the Max user balance
         *
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,account2No,account2Pin,1380);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), -150); // test is the new balance, it's -150 as it's in overdraft
        assertNotNull(atmEngineResponse.getWithdrawalCurrency());
        assertNotNull(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies());
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().size(),2); // we should have four 50 notes so 1 denomination type

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getDemonination(),50); // the only entry should be a 50 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentNumberOfNotes(),20); // 20 50 notes
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentAmount(),1000); // 1000 should be the amount

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getDemonination(),20); // the only entry should be a 20 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getCurrentNumberOfNotes(),19); // 19 20 notes
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getCurrentAmount(),380); // 380 should be the amount

        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with a get balance it should be -150
         */
        AtmEngineRequest atmEngineRequest2 = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,account2No,account2Pin);
        assertNotNull(atmEngineRequest2);
        AtmEngineResponse atmEngineResponse2 = atmEngineService.getAccountBalance(atmEngineRequest2);
        assertNotNull(atmEngineResponse2);
        assertEquals(atmEngineResponse2.getBalance(), -150);
        assertNull(atmEngineResponse2.getWithdrawalCurrency());
        assertEquals(atmEngineResponse2.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse2.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with the Max user account balance it should be 0
         */
        AtmEngineRequest atmEngineRequest3 = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,account2No,account2Pin);
        assertNotNull(atmEngineRequest3);
        AtmEngineResponse atmEngineResponse3 = atmEngineService.getAccountBalance(atmEngineRequest3);
        assertNotNull(atmEngineResponse3);
        assertEquals(atmEngineResponse3.getBalance(), 0);
        assertNull(atmEngineResponse3.getWithdrawalCurrency());
        assertEquals(atmEngineResponse3.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse3.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());
    }

    /*
     * ******************************************
     * Failure junit  test cases
     * ******************************************
     */

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
    public void testInvalidWithdrawalAmountNotaMultipleOfFive() {
        /**
         * Test making an account withdrawal of 7
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,7);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ATM_AMOUNT_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ATM_AMOUNT_ERR.getErrorCode());
    }

    @Test
    public void testInvalidAccountNumber() {
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

    @Test
    public void testInvalidAccountPin() {
        /**
         * Test making a withdrawal with an invalid account pin
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,"8766", 10);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_PIN_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_PIN_ERR.getErrorCode());
    }

    @Test
    public void testEmptyAccountNumber() {
        /**
         * Test making a withdrawal with an invalid accountNo
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,"",accountPin, 10);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
    }

    @Test
    public void testEmptyAccountPin() {
        /**
         * Test making a withdrawal with an invalid account pin
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,"123456789","", 10);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
    }

    @Test
    public void testNullAccountNumber() {
        /**
         * Test making a withdrawal with an invalid accountNo
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,null,accountPin, 10);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
    }

    @Test
    public void testNullAccountPin() {
        /**
         * Test making a withdrawal with an invalid account pin
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,"123456789",null, 10);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0); // we do not get the balance here
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
    }

    @Test
    /**
     * This test case tests a sequence of REST service calls, the sequence is as follows,
     *
     * 1) Make Max allowable withdrawal for user "987654321" - successful
     * 2) Get balance for user "987654321" - successful
     * 3) Get Max allowable withdrawal balance for user "987654321" - successful
     * 4) Make withdrawal for user "123456789" that leaves only 20 in the ATM cash reserves - successful
     * 5) Get balance for user "123456789" - successful
     * 6) Get Max allowable withdrawal balance for user "123456789" - successful
     * 7) Attempt to make a withdrawal of 5 for user "987654321" - failure as user has not funds available
     * 8) Make withdrawal for user "123456789" that leaves only ZERO in the ATM cash reserves - successful
     * 9) Get balance for user "123456789" - successful
     * 10) Get Max allowable withdrawal balance for user "123456789" - successful
     * 11) Attempt to make a withdrawal of 5 for user "123456789" - failure as ATM cash reserves are exhausted
     *
     */
    public void testMakingMultipleAccountWithdrawalsEventuallyExceedingAtmReserves() {
        /**
         * Test making an account withdrawal to the Max user balance (balance + overdraft)
         *
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,account2No,account2Pin,1380);
        AtmEngineResponse atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), -150); // test is the new balance, it's -150 as it's in overdraft
        assertNotNull(atmEngineResponse.getWithdrawalCurrency());
        assertNotNull(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies());
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().size(),2); // we should have four 50 notes so 1 denomination type

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getDemonination(),50); // the only entry should be a 50 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentNumberOfNotes(),20); // 20 50 notes
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentAmount(),1000); // 1000 should be the amount

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getDemonination(),20); // the only entry should be a 20 denomination type
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getCurrentNumberOfNotes(),19); // 19 20 notes
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getCurrentAmount(),380); // 380 should be the amount

        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with a get balance it should be -150
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,account2No,account2Pin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), -150);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with the Max user account balance it should be 0
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,account2No,account2Pin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 0);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());


        /**
         * Test making an account withdrawal for the second user account leaving only 20 in the ATM Cash reserves
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,600);
        atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 200); // test is the new balance
        assertNotNull(atmEngineResponse.getWithdrawalCurrency());
        assertNotNull(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies());
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().size(),3);

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getDemonination(),20);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentNumberOfNotes(),11);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentAmount(),220);

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getDemonination(),10);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getCurrentNumberOfNotes(),30);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(1).getCurrentAmount(),300);

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(2).getDemonination(),5);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(2).getCurrentNumberOfNotes(),16);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(2).getCurrentAmount(),80);

        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with a get balance it should be 200
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 200);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with the Max user account balance it should be 400
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 400);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Test making an account withdrawal for the first user again, this should fail as the user has no available funds
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,account2No,account2Pin,5);
        atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), -150); // test is the new balance
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ACCOUNT_FUNDS_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ACCOUNT_FUNDS_ERR.getErrorCode());

        /**
         * Test making an account withdrawal for the second user again that will exhaust the ATM reserves
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,20);
        atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 180); // test is the new balance
        assertNotNull(atmEngineResponse.getWithdrawalCurrency());
        assertNotNull(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies());
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().size(),1);

        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getDemonination(),5);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentNumberOfNotes(),4);
        assertEquals(atmEngineResponse.getWithdrawalCurrency().getAtmEngineCurrencies().get(0).getCurrentAmount(),20);

        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with a get balance it should be 180
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 180);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());

        /**
         * Now test with the Max user account balance it should be 380
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,accountNo,accountPin);
        assertNotNull(atmEngineRequest);
        atmEngineResponse = atmEngineService.getAccountBalance(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 380);
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.REQUEST_SUCCESS.getErrorCode());


        /**
         * Test making an account withdrawal for the second user again that will exceed the amount held in the ATM reserves
         */
        atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,5);
        atmEngineResponse = atmEngineService.makeAccountWithdrawal(atmEngineRequest);
        assertNotNull(atmEngineResponse);
        assertEquals(atmEngineResponse.getBalance(), 180); // test is the new balance
        assertNull(atmEngineResponse.getWithdrawalCurrency());
        assertEquals(atmEngineResponse.getResponseMessage(), AtmEngineError.ATM_FUNDS_ERR.getResponseDescription());
        assertEquals(atmEngineResponse.getResponseCode(), AtmEngineError.ATM_FUNDS_ERR.getErrorCode());

    }

}