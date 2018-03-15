/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.springframework.stereotype.Service;
import com.jjProj.atmEngine.common.AtmEngineError;
import com.jjProj.atmEngine.common.AtmEngineRequestType;
import com.jjProj.atmEngine.datamodel.AtmEngineConfig;
import com.jjProj.atmEngine.datamodel.AtmEngineCurrency;
import com.jjProj.atmEngine.datamodel.AtmEngineRequest;
import com.jjProj.atmEngine.datamodel.AtmEngineResponse;
import com.jjProj.atmEngine.datamodel.AtmEngineUserAccount;
import com.jjProj.atmEngine.datamodel.WithdrawalCurrency;
import com.jjProj.atmEngine.service.AtmEngineService;

/**
 * ATM Engine Service implementation Class. This class implements AtmEngineService.
 *
 * We annotate this class with @Service so that Spring will pick it up with it's scan.
 *
 */
@Service
public class AtmEngineServiceImpl implements AtmEngineService{

    /**
     * This is the Config user to model the ATM.
     */
    AtmEngineConfig atmEngineConfig = new AtmEngineConfig();

    public AtmEngineConfig getAtmEngineConfig() {
        return atmEngineConfig;
    }

    @Override
    /**
     * This method is used to initialise the ATM Engine.
     *
     * @return AtmEngineConfig - The config loaded for the ATM Engine
     */
    public AtmEngineConfig initAtmEngine() {

        List<AtmEngineUserAccount> atmEngineUserAccounts = new ArrayList<AtmEngineUserAccount>();
        Map<Integer, AtmEngineCurrency> atmEngineCurrencyNotes =  new HashMap<Integer, AtmEngineCurrency>();

        /**
         * TODO : Read in the User Account information from a permanent data source, but
         * for the moment hard code 2 user accounts here
         */
        AtmEngineUserAccount atmEngineUserAcount = new AtmEngineUserAccount("123456789","1234",800,200);
        atmEngineUserAccounts.add(atmEngineUserAcount);
        atmEngineUserAcount = new AtmEngineUserAccount("987654321","4321",1230,150);
        atmEngineUserAccounts.add(atmEngineUserAcount);
        /**
         * set the accounts in the config object
         */
        getAtmEngineConfig().setAtmEngineUserAccounts(atmEngineUserAccounts);

        /**
         * TODO : Read in the ATM machine cash amounts and number of amounts from a permanent data source,
         * but for the moment hard code the  cash amounts and number of amounts here.
         */
        AtmEngineCurrency atmEngineCurrency= new AtmEngineCurrency(50, 20);
        atmEngineCurrencyNotes.put(50, atmEngineCurrency);
        atmEngineCurrency= new AtmEngineCurrency(20, 30);
        atmEngineCurrencyNotes.put(20,atmEngineCurrency);
        atmEngineCurrency= new AtmEngineCurrency(10, 30);
        atmEngineCurrencyNotes.put(10,atmEngineCurrency);
        atmEngineCurrency= new AtmEngineCurrency(5, 20);
        atmEngineCurrencyNotes.put(5,atmEngineCurrency);
        /**
         * set the currency info in the config object
         */
        getAtmEngineConfig().setAtmEngineCurrencyNotes(atmEngineCurrencyNotes);

        calculateAndSetCurrentAtmEngineAmount();

        return getAtmEngineConfig();
    }


    @Override
    /**
     * This method is used for 2 REST service requests as they are very similar
     *  1) getBalance
     *  2) getMaximumWithdrawalBalance
     *
     *  The appropriate user balance amount is found and returned.
     *
     * @param AtmEngineRequest - atmEngineRequest with details of the user
     * @return AtmEngineResponse - Response object
     */
    public AtmEngineResponse getAccountBalance(AtmEngineRequest atmEngineRequest) {

        AtmEngineResponse atmEngineResponse = validateUserRequest(atmEngineRequest);
        if (AtmEngineError.REQUEST_SUCCESS.getErrorCode() == atmEngineResponse.getResponseCode()){
            /**
             * get the account balance or the maximum withdrawal amount and populate the response
             */
            AtmEngineUserAccount atmEngineUserAccount = findAccount(atmEngineRequest.getAccountNo(), atmEngineRequest.getAccountPin());

            if (atmEngineRequest.getAtmEngineRequestType().equals(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE)){
                atmEngineResponse.setBalance(atmEngineUserAccount.getBalance()+atmEngineUserAccount.getOverdraft());
            } else {
                atmEngineResponse.setBalance(atmEngineUserAccount.getBalance());
            }
        }
        return atmEngineResponse;
    }

    @Override
    /**
     * This method is used for the service request to withdraw an amount for a user.
     *
     * @param AtmEngineRequest - atmEngineRequest with details of the user and request
     * @return AtmEngineResponse - Response object
     */
    public AtmEngineResponse makeAccountWithdrawal(AtmEngineRequest atmEngineRequest) {

        AtmEngineResponse atmEngineResponse = validateUserRequest(atmEngineRequest);
        if (AtmEngineError.REQUEST_SUCCESS.getErrorCode() == atmEngineResponse.getResponseCode()){

            /**
             * Attempt the withdrawal
             *
             * get the account balance and populate the response
             */
            AtmEngineUserAccount atmEngineUserAccount = findAccount(atmEngineRequest.getAccountNo(), atmEngineRequest.getAccountPin());
            atmEngineResponse.setBalance(atmEngineUserAccount.getBalance());
            /**
             * Check if the ATM has funds to cover the withdrawal amount
             */
            if (getAtmEngineConfig().getAtmMachineCurrentBalance() >= atmEngineRequest.getWithdrawalAmount()){
                /**
                 * Check the user has enough funds
                 */
                if ((atmEngineUserAccount.getBalance()+atmEngineUserAccount.getOverdraft()) >= atmEngineRequest.getWithdrawalAmount()){

                    /**
                     * Find if the notes exist to make the withdrawal
                     */
//                    for (Map.Entry<Integer, AtmEngineCurrency> entry : getAtmEngineConfig().getAtmEngineCurrencyNotes().entrySet()) {

                    //get sorted denominations
                    TreeSet<Integer> denominations = new TreeSet<Integer>(getAtmEngineConfig().getAtmEngineCurrencyNotes().keySet());
                    Iterator<Integer> iter = denominations.descendingIterator();

/*                    int amountToWithdraw = atmEngineRequest.getWithdrawalAmount();
                    while(amountToWithdraw > 0 ){
                        int denomination = iter.next();
                        int noOfNotes = amountToWithdraw< denomination ? 0 : amountToWithdraw/denomination;
                        returnedMap.put(denomination, noOfNotes);
                        amountToWithdraw = amountToWithdraw - (denomination * noOfNotes);
                        reduceBalance(denomination, noOfNotes);
                    }
                    */
                    /**
                     * Make the withdrawal from the currencies
                     */

                    /**
                     * Make the withdrawal from the user account
                     */

                    /**
                     * Calculate the ATM current balance again after the withdrawal
                     */
                    calculateAndSetCurrentAtmEngineAmount();
                } else {
                    atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_FUNDS_ERR.getErrorCode());
                    atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_FUNDS_ERR.getResponseDescription());
                }
            } else {
                atmEngineResponse.setResponseCode(AtmEngineError.ATM_AMOUNT_ERR.getErrorCode());
                atmEngineResponse.setResponseMessage(AtmEngineError.ATM_AMOUNT_ERR.getResponseDescription());
            }
        }
        return atmEngineResponse;

    }

    /**
     * This method is used to validate the user request.
     *
     * @param AtmEngineRequest - atmEngineRequest with details of the user and request
     * @return AtmEngineResponse - JSON response object
     */
    private AtmEngineResponse validateUserRequest(AtmEngineRequest atmEngineRequest) {

        AtmEngineResponse atmEngineResponse = new AtmEngineResponse(0,AtmEngineError.REQUEST_SUCCESS.getErrorCode(),AtmEngineError.REQUEST_SUCCESS.getResponseDescription());
        /**
         * This is checking the following,
         *
         * 1) The Account number is valid and exists in the known account number list
         * 2) The pin is valid and matches the account pin
         * 3) The withdrawal amount is a multiple of 5 as this is what the ATM can only dispense
         */
        if (atmEngineRequest.getAccountNo() == null &&
                atmEngineRequest.getAccountNo() == "" ){
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        } else if (atmEngineRequest.getAccountPin() == null &&
                atmEngineRequest.getAccountPin() == "") {
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        } else if (!accountNumberIsKnown(atmEngineRequest.getAccountNo())){
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_NUMBER_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_NUMBER_ERR.getResponseDescription());
        }  else if (!accountPinMatches(atmEngineRequest.getAccountNo(),atmEngineRequest.getAccountPin())){
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_PIN_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_PIN_ERR.getResponseDescription());
        } else if ((atmEngineRequest.getWithdrawalAmount() %5) >0 ) {
            atmEngineResponse.setResponseCode(AtmEngineError.ATM_AMOUNT_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ATM_AMOUNT_ERR.getResponseDescription());
        }

        return atmEngineResponse;
    }

    /**
     * This method is used to validate if the user is known on the ATM.
     *
     * @param String - accountNumber for the user.
     * @return boolean - true if user is known on the ATM
     */
    private boolean accountNumberIsKnown(String accountNumber){
        for (AtmEngineUserAccount atmEngineUserAcount: getAtmEngineConfig().getAtmEngineUserAccounts()){
            if (atmEngineUserAcount.getAccountNo().equalsIgnoreCase(accountNumber)){
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to check if the user account number and user account pin matches
     * the information held in the ATM.
     *
     * @param String - accountNumber
     * @param String - accountPin
     * @return boolean - true if the user and pin matches
     */
    private boolean accountPinMatches(String accountNumber, String accountPin){
        for (AtmEngineUserAccount atmEngineUserAcount: getAtmEngineConfig().getAtmEngineUserAccounts()){
            if (atmEngineUserAcount.getAccountNo().equalsIgnoreCase(accountNumber) &&
                    atmEngineUserAcount.getAccountPin().equalsIgnoreCase(accountPin)){
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to check if the user account number and user account pin matches
     * the information held in the ATM and return the user account object.
     *
     * @param String - accountNumber
     * @param String - accountPin
     * @return AtmEngineUserAccount - The user account information held in the ATM
     */
    private AtmEngineUserAccount findAccount(String accountNumber, String accountPin){
        for (AtmEngineUserAccount atmEngineUserAcount: getAtmEngineConfig().getAtmEngineUserAccounts()){
            if (atmEngineUserAcount.getAccountNo().equalsIgnoreCase(accountNumber) &&
                    atmEngineUserAcount.getAccountPin().equalsIgnoreCase(accountPin)){
                return atmEngineUserAcount;
            }
        }
        return null;
    }

    /**
     * This method is used to calculate the current total amount now held in the ATM.
     *
     * @return int - The total amount now held in the ATM
     */
    private int calculateAndSetCurrentAtmEngineAmount(){
        int atmEngineCurrentAmount = 0;

        for (Map.Entry<Integer, AtmEngineCurrency> entry : getAtmEngineConfig().getAtmEngineCurrencyNotes().entrySet()) {
            //int demonination = entry.getKey();
            AtmEngineCurrency demoninationDetail = entry.getValue();
            atmEngineCurrentAmount += demoninationDetail.getCurrentAmount();
        }
        getAtmEngineConfig().setAtmMachineCurrentBalance(atmEngineCurrentAmount);
        return  getAtmEngineConfig().getAtmMachineCurrentBalance();
    }

    /**
     * This method is used to determine if a withdrawal can be made from the ATM.
     *
     * This method will calculate the denomination and amounts of notes required to satisfy the withdrawal.
     *
     * @param AtmEngineRequest - details of the withdrawal request.
     * @return WithdrawalCurrency - The details of the withdrawal.
     */
    private WithdrawalCurrency findCurrencyMatch(AtmEngineRequest atmEngineRequest){
        WithdrawalCurrency withdrawalCurrency = new WithdrawalCurrency();

        int currenctWithdrawalAmount = atmEngineRequest.getWithdrawalAmount();
        while (currenctWithdrawalAmount> 0){

        }
        return withdrawalCurrency;
    }

}
