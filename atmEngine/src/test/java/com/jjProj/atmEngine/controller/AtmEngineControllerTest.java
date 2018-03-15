/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.controller;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.jjProj.atmEngine.service.impl.AtmEngineServiceImpl;

import java.util.Arrays;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class})
//@WebAppConfiguration
public class AtmEngineControllerTest {
    @Test
    public void testGetBalance() throws IOException {

//        assertTrue("OK", true);
    }


    @Test
    public void testGetMaximumWithdrawalBalance() throws IOException {
        assertTrue("OK", true);
    }



    @Test
    public void testMakeAccountWithdrawal() throws IOException {
        assertTrue("OK", true);
    }

}
