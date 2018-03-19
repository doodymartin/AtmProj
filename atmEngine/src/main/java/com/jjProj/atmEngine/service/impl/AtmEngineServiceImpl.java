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
     * This is the Configuration object used to model the ATM.
     */
    AtmEngineConfig atmEngineConfig = new AtmEngineConfig();

    @Override
    /**
     * This method is used to initialise the ATM Engine on startup of the ATM Engine application.
     *
     * TODO : This Initialisation (account user and currency information) information needs to
     * come from a persistent data source and not hard coded as below.
     *
     * @return AtmEngineConfig - The config object loaded for the ATM Engine
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

        /**
         * TODO : Identify any failure reasons for the ATM config
         */
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
     * @param AtmEngineRequest - atmEngineRequest with details of the user request
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
                if (atmEngineUserAccount.getBalance() <0 ){
                    atmEngineResponse.setBalance(atmEngineUserAccount.getOverdraft());
                }else {
                    atmEngineResponse.setBalance(atmEngineUserAccount.getBalance()+atmEngineUserAccount.getOverdraft());
                }
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
     * @param AtmEngineRequest - atmEngineRequest with details of the user request
     * @return AtmEngineResponse - Response object
     */
    public AtmEngineResponse makeAccountWithdrawal(AtmEngineRequest atmEngineRequest) {

        AtmEngineResponse atmEngineResponse = validateUserRequest(atmEngineRequest);
        if (AtmEngineError.REQUEST_SUCCESS.getErrorCode() == atmEngineResponse.getResponseCode()){

            /**
             * Attempt the withdrawal.
             *
             * get the account balance and populate the response
             */
            AtmEngineUserAccount atmEngineUserAccount = findAccount(atmEngineRequest.getAccountNo(), atmEngineRequest.getAccountPin());
            atmEngineResponse.setBalance(atmEngineUserAccount.getBalance());
            /**
             * Check if the ATM has funds to cover the withdrawal amount for this user
             */
            if (getAtmEngineConfig().getAtmMachineCurrentBalance() >= atmEngineRequest.getWithdrawalAmount()){
                /**
                 * Check for a starting negative user balance then Check the user has enough funds for this withdrawal.
                 *
                 */
                int userBalance = 0;
                if (atmEngineUserAccount.getBalance() > 0){
                    userBalance = atmEngineUserAccount.getBalance();
                }
                if ((userBalance+atmEngineUserAccount.getOverdraft()) >= atmEngineRequest.getWithdrawalAmount()){

                    /**
                     * Find if the denominations and number of notes for the withdrawal exist in the ATM reserves
                     */
                    WithdrawalCurrency withdrawalCurrency = findCurrencyMatch(atmEngineRequest);
                    /**
                     * if withdrawalCurrency is null we failed to find the correct notes in the ATM reserves
                     *
                     */
                    if (withdrawalCurrency != null){
                        /**
                         * Make the withdrawal from the user account, both the balance and the overdraft if required
                         */
                        int newBalance = atmEngineUserAccount.getBalance()-atmEngineRequest.getWithdrawalAmount();
                        atmEngineUserAccount.setBalance(newBalance);

                        // now deal with updating the user overdraft
                        if(newBalance<0){
                            int newOverDraft = newBalance-(atmEngineUserAccount.getBalance());
                            atmEngineUserAccount.setOverdraft(newOverDraft);
                        }
                        /**
                         * Now make the withdrawal from the ATM machine cash reserves, based on the dominations identified for the withdrawal.
                         */
                        if(recalculateAtmMachineCashReserves(withdrawalCurrency)){

                            /**
                             * set the balance in the response
                             */
                            atmEngineResponse.setBalance(atmEngineUserAccount.getBalance());

                            /**
                             * set the withdrawal details for the Currency notes in the response
                             */
                            atmEngineResponse.setWithdrawalCurrency(withdrawalCurrency);
                        } else {
                            atmEngineResponse.setResponseCode(AtmEngineError.ATM_FUNDS_ERR.getErrorCode());
                            atmEngineResponse.setResponseMessage(AtmEngineError.ATM_FUNDS_ERR.getResponseDescription());
                        }
                    } else {
                        atmEngineResponse.setResponseCode(AtmEngineError.ATM_FUNDS_ERR.getErrorCode());
                        atmEngineResponse.setResponseMessage(AtmEngineError.ATM_FUNDS_ERR.getResponseDescription());
                    }

                    /**
                     * Calculate the new ATM current balance again after the withdrawal
                     */
                    calculateAndSetCurrentAtmEngineAmount();
                } else {
                    atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_FUNDS_ERR.getErrorCode());
                    atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_FUNDS_ERR.getResponseDescription());
                }
            } else {
                atmEngineResponse.setResponseCode(AtmEngineError.ATM_FUNDS_ERR.getErrorCode());
                atmEngineResponse.setResponseMessage(AtmEngineError.ATM_FUNDS_ERR.getResponseDescription());
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
         * 1) Check that the withdrawal amount is not negative
         * 1) The Account number is valid and exists in the known account number list
         * 2) The pin is valid and matches the account pin
         * 3) The withdrawal amount is a multiple of 5 as this is what the ATM can only dispense
         */
        if (atmEngineRequest.getWithdrawalAmount() < 0 ){
            atmEngineResponse.setResponseCode(AtmEngineError.WITHDRAWAL_AMOUNT_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.WITHDRAWAL_AMOUNT_ERR.getResponseDescription());
        } else if (atmEngineRequest.getAccountNo() == null ||
                atmEngineRequest.getAccountNo() == "" ){
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        } else if (atmEngineRequest.getAccountPin() == null ||
                atmEngineRequest.getAccountPin() == "") {
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_LOGIN_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_LOGIN_ERR.getResponseDescription());
        } else if (!accountNumberIsKnown(atmEngineRequest.getAccountNo())){
            atmEngineResponse.setResponseCode(AtmEngineError.ACCOUNT_NUMBER_ERR.getErrorCode());
            atmEngineResponse.setResponseMessage(AtmEngineError.ACCOUNT_NUMBER_ERR.getResponseDescription());
        }  else if (findAccount(atmEngineRequest.getAccountNo(),atmEngineRequest.getAccountPin()) == null){
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
     * This method is used to calculate the current total cash amount now held in the ATM as well as
     * calculating the amount held in the ATM per currency demonination.
     *
     * @return int - The total amount now held in the ATM
     */
    private int calculateAndSetCurrentAtmEngineAmount(){
        int atmEngineCurrentAmount = 0;

        for (Map.Entry<Integer, AtmEngineCurrency> entry : getAtmEngineConfig().getAtmEngineCurrencyNotes().entrySet()) {
            AtmEngineCurrency demoninationDetail = entry.getValue();
            demoninationDetail.recalculateCurrentAmount();
            atmEngineCurrentAmount += demoninationDetail.getCurrentAmount();
        }
        getAtmEngineConfig().setAtmMachineCurrentBalance(atmEngineCurrentAmount);
        return  getAtmEngineConfig().getAtmMachineCurrentBalance();
    }

    /**
     * This method is used to determine if a withdrawal can be made from the ATM.
     *
     * This method will calculate the denomination and amounts of notes required to satisfy the user withdrawal.
     *
     * @param AtmEngineRequest - details of the withdrawal request.
     * @return WithdrawalCurrency - The details of the withdrawal, null is withdrawal cannot be made.
     */
    private WithdrawalCurrency findCurrencyMatch(AtmEngineRequest atmEngineRequest){
        WithdrawalCurrency withdrawalCurrency = new WithdrawalCurrency();
        List<AtmEngineCurrency> atmEngineCurrencies = new ArrayList<AtmEngineCurrency>();
        withdrawalCurrency.setAtmEngineCurrencies(atmEngineCurrencies);

        // Get sorted denominations in decending order
        TreeSet<Integer> denominations = new TreeSet<Integer>(getAtmEngineConfig().getAtmEngineCurrencyNotes().keySet());
        Iterator<Integer> iter = denominations.descendingIterator();

        int amountToWithdraw = atmEngineRequest.getWithdrawalAmount();
        while(amountToWithdraw > 0 && iter.hasNext()){
            int denomination = iter.next();
            AtmEngineCurrency atmEngineCurrency = atmEngineConfig.getAtmEngineCurrencyNotes().get(denomination);
            AtmEngineCurrency atmEngineCurrencyClone = null;
            try{
                /**
                 * Make a copy of the ATM reserves cash details as we only want to make changes to it once we
                 * know the withdrawal can be make successfully.
                 */
                atmEngineCurrencyClone =  (AtmEngineCurrency)atmEngineCurrency.clone();
                boolean addedDenominationNotes = false;
                if (atmEngineCurrencyClone.getCurrentAmount() > 0){
                    AtmEngineCurrency atmEngineCurrencyToStore = new AtmEngineCurrency(denomination, 0);
                    int numberOfNotesInDenomination = atmEngineCurrencyClone.getCurrentNumberOfNotes();
                    for(int i =0; i<numberOfNotesInDenomination && amountToWithdraw > 0;i++){
                        if (amountToWithdraw >= denomination) {

                            /**
                             * we only want to send notes we take as part of the withdrawal in the REST response
                             */
                            addedDenominationNotes = true;
                            amountToWithdraw = amountToWithdraw-denomination;
                            /**
                             * TODO :: The withdrawal can still fail so we should not change the ATM values here until we are sure
                             * the withdrawal can be successful.
                             */
                            atmEngineCurrencyClone.setCurrentNumberOfNotes((atmEngineCurrencyClone.getCurrentNumberOfNotes()-1));
                            /**
                             * increment the number of notes for this denomination
                             */
                            atmEngineCurrencyToStore.setCurrentNumberOfNotes((atmEngineCurrencyToStore.getCurrentNumberOfNotes()+1));

                            /**
                             * Recalculate the amount associated with the denomination after changing the of notes
                             */
                            atmEngineCurrencyToStore.recalculateCurrentAmount();
                            atmEngineCurrencyClone.recalculateCurrentAmount();
                        }
                    }
                    if (addedDenominationNotes){
                        withdrawalCurrency.getAtmEngineCurrencies().add(atmEngineCurrencyToStore);
                    }
                }

            } catch(CloneNotSupportedException exception){
                /**
                 * We failed to copy a currency object so force an error response from this method.
                 */
                amountToWithdraw = -1;
            }
        }

        /**
         * If we failed to satisfy the total withdrawal amount then it's an error
         */
        if (amountToWithdraw >0){
            withdrawalCurrency =null;
        }
        return withdrawalCurrency;
    }

    /**
     * This method is used to make the correct deductions to the cash reserves held in the
     * ATM Machine after a user withdrawal.
     *
     * This method will make the correct changes to each denomination for the user withdrawal.
     *
     * @param WithdrawalCurrency - the details of the user withdrawal.
     * @return boolean - true if successful in changes ATM Machine reserves.
     */
    private boolean recalculateAtmMachineCashReserves(WithdrawalCurrency withdrawalCurrency){
        boolean madeSuccessfulAtmMachineWithdrawal =true;

        /**
         * Loop through all the demoninations in the user cash withdrawal and make the deductions from the ATM reserves for that demonination.
         */
        for (int i =0; i<withdrawalCurrency.getAtmEngineCurrencies().size() && madeSuccessfulAtmMachineWithdrawal;i++){
            AtmEngineCurrency atmEngineCurrency = withdrawalCurrency.getAtmEngineCurrencies().get(i);
            AtmEngineCurrency atmEngineCurrencyToChange = getAtmEngineConfig().getAtmEngineCurrencyNotes().get(atmEngineCurrency.getDemonination());
            if (atmEngineCurrencyToChange != null){
                atmEngineCurrencyToChange.setCurrentNumberOfNotes((atmEngineCurrencyToChange.getCurrentNumberOfNotes()-atmEngineCurrency.getCurrentNumberOfNotes()));
            } else {
                madeSuccessfulAtmMachineWithdrawal = false;
            }
        }
        return madeSuccessfulAtmMachineWithdrawal;
    }

    public AtmEngineConfig getAtmEngineConfig() {
        return atmEngineConfig;
    }
    public void setAtmEngineConfig(AtmEngineConfig atmEngineConfig) {
        this.atmEngineConfig = atmEngineConfig;
    }

}
