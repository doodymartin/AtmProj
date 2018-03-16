/*********************************************************************************************************************************
* Copyright (c) 2017. martin.doody@gmail.com All rights reserved.
*
* This software is put together for investigation purposes. Use at your own risk.
********************************************************************************************************************************/

package com.jjProj.atmEngine.controller;

import static org.junit.Assert.*;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.test.web.servlet.MockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.jjProj.atmEngine.main.AtmEngine;

/**
 * TODO : I have not had a chance to get any test coverage in here.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = AtmEngine.class)
@AutoConfigureMockMvc
public class AtmEngineControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Autowired
    private AtmEngineController atmEngineController;

//    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(atmEngineController).build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.webApplicationContext).build();
    }

    @Test
    public void testGetBalance() throws Exception {

        //Mocking Controller
/*        atmEngineController = mock(AtmEngineController.class);

         this.mockMvc.perform(get("/")
                 .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                 .andExpect(status().isOk())
                 .andExpect(content().mimeType(MediaType.APPLICATION_JSON));
*/
//        this.mockMvc.perform(get("/getBalance")).andDo(print()).andExpect(status().isOk())
//        .andExpect(content().string(containsString("Hello World")));


//        testRestTemplate.getForObject(baseUrl + "/getBalance", String.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/getBalance")
                .param("accountNo", "123456789")
                .param("accountPin", "1234")
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Succeeded")));

/*        this.mockMvc.perform(get("/getBalance")
                .contentType(contentType)
                .param("accountNo", "123456789")
                .param("accountPin", "1234")
                ).andDo(print())
                .andExpect(status().isOk())
        .andExpect(content().string(containsString("Succeeded")));
*/
        assertTrue("OK", true);
    }


    @Test
    public void testGetMaximumWithdrawalBalance() throws IOException {
        assertTrue("OK", true);
    }

    @Test
    public void testMakeAccountWithdrawal() throws IOException {
        assertTrue("OK", true);
    }

    @Test
    public void contexLoads() throws Exception {
        assertThat(atmEngineController).isNotNull();
    }
}
