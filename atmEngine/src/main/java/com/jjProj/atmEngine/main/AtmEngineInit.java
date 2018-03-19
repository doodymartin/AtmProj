/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.main;

import javax.annotation.Resource;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.jjProj.atmEngine.service.impl.AtmEngineServiceImpl;

/**
 * ATM Engine class to help initialise the Engine..
 *
 * Create a Spring bean component so that it can get picked up by the context and make it implement the
 * ApplicationListener interface so that we can trigger the ATM Engine init on startup of the applicaiton
 * triggered from the ApplicationReady Event.
 */
@Component
public class AtmEngineInit implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * Wire in the Service layer bean so that we can see the ATM engine init method
     */
    @Resource(name="atmEngineServiceImpl")
    private AtmEngineServiceImpl atmEngineService;

    /**
     * Create a listener for the "ApplicationReadyEvent" event so that we can run the ATM engine init
     * method on startup of the Spring Context.
     *
     * The ATM Engine init will do the following,
     * 1) Load in the user accounts
     * 2) Load in the ATM cash reserves
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if(atmEngineService.initAtmEngine() == null){
            System.out.println("failed to initialise the ATM Engine configuration");
        } else {
            System.out.println("Successfully loaded ATM Engine configuration");
        }
    }
}