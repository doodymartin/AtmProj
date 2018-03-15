/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/
package com.jjProj.atmEngine.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.jjProj.atmEngine.service.impl.AtmEngineServiceImpl;

/**
 * ATM Engine Main class.
 *
 * Create a Sprin Boot application and scan for Spring resurces.
 */
@SpringBootApplication
@ComponentScan("com.jjProj")
public class AtmEngine {

    public static void main(String[] args) {

        /**
         * Start the ATM Engine
         */
        ConfigurableApplicationContext atmEngineApplication = SpringApplication.run(AtmEngine.class, args);

        /**
         * Initialise the ATM engine by doing the following,..
         *
         * 1) Load User Account information
         * 2) Initial the ATM engine cash
         */
        AtmEngineServiceImpl atmEngineService = (AtmEngineServiceImpl)atmEngineApplication.getBean("atmEngineServiceImpl");
        atmEngineService.initAtmEngine();

    }
}





