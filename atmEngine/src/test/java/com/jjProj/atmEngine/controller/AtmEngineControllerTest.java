/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.controller;

import static org.junit.Assert.*;

import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jjProj.atmEngine.datamodel.AtmEngineConfig;
import com.jjProj.atmEngine.main.AtmEngine;
import com.jjProj.atmEngine.service.impl.AtmEngineServiceImpl;

/**
 * Test class for integration testing the Controller REST services for the ATM Engine
 *
 * TODO : There is scope for more detailed edge test cases around account limits and failure test cases.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = AtmEngine.class)
public class AtmEngineControllerTest {
    String accountNo = "123456789";
    String accountPin = "1234";

    @LocalServerPort
    private int atmEnginePort;

    @Autowired
    private AtmEngineServiceImpl atmEngineService;

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setup() {
        AtmEngineConfig atmEngineConfig = atmEngineService.initAtmEngine();
        atmEngineService.setAtmEngineConfig(atmEngineConfig);
    }

    @Test
    public void testGetBalance() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> atmEngineResponse = restTemplate.exchange(
                createURLForAtmEngine("/getBalance?accountNo=123456789&accountPin=1234"),
                HttpMethod.GET,
                entity,
                String.class);
        String expectedAtmEngineResponse = "{\"balance\":800,\"responseCode\":0,\"responseMessage\":\"Request Succeeded.\",\"withdrawalCurrency\":null}";
        JSONAssert.assertEquals(expectedAtmEngineResponse, atmEngineResponse.getBody(), false);
    }

    @Test
    public void testGetMaximumWithdrawalBalance() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> atmEngineResponse = restTemplate.exchange(
                createURLForAtmEngine("/getMaximumWithdrawalBalance?accountNo=123456789&accountPin=1234"),
                HttpMethod.GET,
                entity,
                String.class);
        String expectedAtmEngineResponse = "{\"balance\":1000,\"responseCode\":0,\"responseMessage\":\"Request Succeeded.\",\"withdrawalCurrency\":null}";
        JSONAssert.assertEquals(expectedAtmEngineResponse, atmEngineResponse.getBody(), false);
    }

    @Ignore
    public void testMakeAccountWithdrawal() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> atmEngineResponse = restTemplate.exchange(
                createURLForAtmEngine("/makeAccountWithdrawal?accountNo=123456789&accountPin=1234&withdrawalAmount=675"),
                HttpMethod.GET,
                entity,
                String.class);
        String expectedAtmEngineResponse = "{\"balance\":125,\"responseCode\":0,\"responseMessage\":\"Request Succeeded.\",\"withdrawalCurrency\":{\"atmEngineCurrencies\":[{\"demonination\":50,\"currentNumberOfNotes\":13,\"currentAmount\":650},{\"demonination\":20,\"currentNumberOfNotes\":1,\"currentAmount\":20},{\"demonination\":5,\"currentNumberOfNotes\":1,\"currentAmount\":5}]}}";
        JSONAssert.assertEquals(expectedAtmEngineResponse, atmEngineResponse.getBody(), false);
    }

    private String createURLForAtmEngine(String uri) {
        return "http://localhost:"+ atmEnginePort+uri;
    }

}
