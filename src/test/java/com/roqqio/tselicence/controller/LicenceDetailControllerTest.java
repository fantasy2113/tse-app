package com.roqqio.tselicence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roqqio.tselicence.core.DbSetup;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.entities.User;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.core.interfaces.repositories.IUserRepository;
import com.roqqio.tselicence.security.Authenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import testutil.DbUtilsTest;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LicenceDetailControllerTest {
    @Autowired
    Authenticator authenticator;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    ILicenceDetailRepository licenceDetailRepository;
    @Autowired
    DbSetup dbSetup;
    @Autowired
    Environment env;

    LicenceDetail licenceDefault;
    User user;
    Cookie cookie;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);
        DbUtilsTest.insertDefaultUser(env, userRepository);

        user = DbUtilsTest.getValidUser(userRepository);
        cookie = new Cookie("ctoken", authenticator.getToken(user.getUsername(), user.getPassword()).get());
        licenceDefault = new LicenceDetail();

        licenceDefault.setLicenceId(1);
        licenceDefault.setBranchNumber(1);
        licenceDefault.setTillExternalId(1);
    }

    @Test
    void licences() throws Exception {
        List<LicenceDetail> expected = DbUtilsTest.saveDemoLicencesDetailToDbAndReturnAsList(licenceDetailRepository);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/internal/licences/detail/{licence_id}", "1")
                .cookie(cookie)).andExpect(status().isOk()).andReturn();

        assertEquals(objectMapper.writeValueAsString(expected), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void filter() throws Exception {
        List<LicenceDetail> itmes = DbUtilsTest.saveDemoLicencesDetailToDbAndReturnAsList(licenceDetailRepository);

        List<LicenceDetail> expected = new ArrayList<>();
        expected.add(itmes.get(0));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/internal/licence/detail/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(itmes.get(0)))
                .cookie(cookie)).andExpect(status().isOk()).andReturn();

        assertEquals(objectMapper.writeValueAsString(expected), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void delete() throws Exception {
        licenceDetailRepository.save(licenceDefault);

        assertTrue(licenceDetailRepository.exists(1));

        mvc.perform(MockMvcRequestBuilders.delete("/internal/licence/detail/delete/{id}", "1")
                .cookie(cookie)).andExpect(status().is(204));

        assertFalse(licenceDetailRepository.exists(1));
    }

    @Test
    void testDelete() throws Exception {
        int id = 1;

        licenceDetailRepository.save(licenceDefault);
        licenceDefault.setId(id);

        assertTrue(licenceDetailRepository.exists(id));

        mvc.perform(MockMvcRequestBuilders.delete("/internal/licence/detail/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licenceDefault))
                .cookie(cookie)).andExpect(status().is(204));

        assertFalse(licenceDetailRepository.exists(1));
    }

    @Test
    void update() throws Exception {
        int id = 1;

        licenceDetailRepository.save(licenceDefault);
        assertTrue(licenceDetailRepository.exists(id));

        LicenceDetail licenceDetail = licenceDetailRepository.get(id).get();

        assertEquals(1, licenceDetail.getBranchNumber());
        licenceDefault.setBranchNumber(3);

        mvc.perform(MockMvcRequestBuilders.put("/internal/licence/detail/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licenceDefault))
                .cookie(cookie)).andExpect(status().isOk());

        assertTrue(licenceDetailRepository.exists(1));
        assertEquals(3, licenceDefault.getBranchNumber());
    }

    @Test
    void save() throws Exception {
        int id = 1;

        assertFalse(licenceDetailRepository.exists(id));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/internal/licence/detail/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licenceDefault))
                .cookie(cookie)).andExpect(status().isOk()).andReturn();

        licenceDefault.setId(id);

        LicenceDetail result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceDetail.class);
        result.setDateRegistered(licenceDefault.getDateRegistered());

        assertEquals(objectMapper.writeValueAsString(licenceDefault), objectMapper.writeValueAsString(result));
    }
}