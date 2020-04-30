package com.roqqio.tselicence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roqqio.tselicence.controller.responeses.Token;
import com.roqqio.tselicence.core.DbSetup;
import com.roqqio.tselicence.core.interfaces.repositories.IUserRepository;
import com.roqqio.tselicence.security.Authenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import testutil.DbUtilsTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    DbSetup dbSetup;
    @Autowired
    Environment env;
    @Autowired
    private Authenticator authenticator;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);
        DbUtilsTest.insertDefaultUser(env, userRepository);
    }

    @Test
    void getTokenShouldReturnValidKey() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/internal/auth")
                .header("username", "admin").header("password", "admin"))
                .andExpect(status().isOk()).andReturn();

        Token result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Token.class);

        assertFalse(authenticator.isFail(result.getToken()));
    }

    @Test
    void getTokenShouldReturnNonValidKey() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/internal/auth")
                .header("username", "").header("password", ""))
                .andExpect(status().is(401)).andReturn();

        Token result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Token.class);

        assertEquals(new Token().getToken(), result.getToken());
    }
}