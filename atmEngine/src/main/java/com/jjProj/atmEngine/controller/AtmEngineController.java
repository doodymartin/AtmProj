/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jjProj.atmEngine.common.AtmEngineRequestType;
import com.jjProj.atmEngine.datamodel.AtmEngineRequest;
import com.jjProj.atmEngine.datamodel.AtmEngineResponse;
import com.jjProj.atmEngine.service.impl.AtmEngineServiceImpl;

/**
 * Declare as a Spring Rest Controller and scan for Spring bean resources
 */
@RestController
@ComponentScan("com.jjProj")
public class AtmEngineController {

    /**
     * Wire in the Service layer bean
     */
    @Resource(name="atmEngineServiceImpl")
    private AtmEngineServiceImpl atmEngineService;

    /**
     * This method is used to map the "getBalance" HTTP GET request.
     *
     * @param String - accountNo the user account number
     * @param String - accountNo the user account pin
     * @return AtmEngineResponse - JSON response object
     */
    @RequestMapping(method=RequestMethod.GET, value="/getBalance")
    public AtmEngineResponse getBalance(@RequestParam(value="accountNo", required=true) String accountNo,
            @RequestParam(value="accountPin", required=true) String accountPin) {

        /**
         * Create a request object
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_BALANCE,accountNo,accountPin);
        /**
         * Process the request and generate a response
         */
        AtmEngineResponse atmEngineResponse = getAtmEngineService().getAccountBalance(atmEngineRequest);
        /**
         * return the response via a JSON ojbect
         */
        return atmEngineResponse;
    }

    /**
     * This method is used to map the "getMaximumWithdrawalBalance" HTTP GET request.
     *
     * @param String - accountNo the user account number
     * @param String - accountNo the user account pin
     * @return AtmEngineResponse - JSON response object
     */
    @RequestMapping(method=RequestMethod.GET, value="/getMaximumWithdrawalBalance")
    public AtmEngineResponse getMaximumWithdrawalBalance(@RequestParam(value="accountNo", required=true) String accountNo,
            @RequestParam(value="accountPin", required=true) String accountPin) {

        /**
         * Create a request object
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_MAXIMUM_WITHDRAWAL_BALANCE,accountNo,accountPin);
        /**
         * Process the request and generate a response
         */
        AtmEngineResponse atmEngineResponse = getAtmEngineService().getAccountBalance(atmEngineRequest);
        /**
         * return the response via a JSON ojbect
         */
        return atmEngineResponse;
    }


    /**
     * This method is used to map the "makeAccountWithdrawal" HTTP GET request.
     *
     * @param String - accountNo the user account number
     * @param String - accountNo the user account pin
     * @param int - withdrawalAmount the amount requested by the user
     * @return AtmEngineResponse - JSON response object
     */
    @RequestMapping(method=RequestMethod.GET, value="/makeAccountWithdrawal")
    public AtmEngineResponse makeAccountWithdrawal(@RequestParam(value="accountNo", required=true) String accountNo,
            @RequestParam(value="accountPin", required=true) String accountPin,
            @RequestParam(value="withdrawalAmount", required=true) int withdrawalAmount) {

        /**
         * Create a request object
         */
        AtmEngineRequest atmEngineRequest = new AtmEngineRequest(AtmEngineRequestType.ATM_WITHDRAWAL,accountNo,accountPin,withdrawalAmount);
        /**
         * Process the request and generate a response
         */
        AtmEngineResponse atmEngineResponse = getAtmEngineService().makeAccountWithdrawal(atmEngineRequest);
        /**
         * return the response via a JSON ojbect
         */
        return atmEngineResponse;
    }

    public AtmEngineServiceImpl getAtmEngineService() {
        return atmEngineService;
    }

    public void setAtmEngineService(AtmEngineServiceImpl atmEngineService) {
        this.atmEngineService = atmEngineService;
    }

}