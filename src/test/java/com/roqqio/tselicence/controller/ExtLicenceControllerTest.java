package com.roqqio.tselicence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roqqio.tselicence.controller.responeses.LicenceResponse;
import com.roqqio.tselicence.core.DbSetup;
import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import com.roqqio.tselicence.core.entities.User;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import com.roqqio.tselicence.core.interfaces.repositories.ILicenceRepository;
import com.roqqio.tselicence.core.interfaces.repositories.IUserRepository;
import com.roqqio.tselicence.security.Authenticator;
import com.roqqio.tselicence.security.UrlSaveAes;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExtLicenceControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IUserRepository userRepository;
    User user;
    String token;
    @Autowired
    UrlSaveAes aes;
    @Autowired
    private ILicenceRepository licenceRepository;
    @Autowired
    private ILicenceDetailRepository licenceDetailRepository;
    @Autowired
    private Authenticator authenticator;
    @Autowired
    private MockMvc mvc;
    @Autowired
    DbSetup dbSetup;
    @Autowired
    Environment env;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);
        DbUtilsTest.insertDefaultUser(env, userRepository);
        user = DbUtilsTest.getValidUser(userRepository);
        token = authenticator.getToken(user.getUsername(), user.getPassword()).get();
    }

    @Test
    void find() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(1);
        licenceDetailRepository.save(licenceDetail);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licenceDetail.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licenceDetail.getTillExternalId()))
                .param("token", token)).andExpect(status().isOk()).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        LicenceResponse licenceResponse = new LicenceResponse(licence, licenceDetail);

        String licenceNumber = result.getLicenceNumber();

        assertEquals(licenceNumber, aes.encrypt(licence.getLicenceNumber()).get());

        result.setLicenceNumber(aes.decrypt(licenceNumber).get());

        assertEquals(result.toString(), licenceResponse.toString());
    }

    @Test
    void findWithoutEncoding() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(1);
        licenceDetailRepository.save(licenceDetail);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", licence.getLicenceNumber())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licenceDetail.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licenceDetail.getTillExternalId()))
                .param("token", token)).andExpect(status().is(404)).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        assertNotEquals(licence.getLicenceNumber(), result.getLicenceNumber());
        assertEquals(null, result.getLicenceNumber());
    }


    @Test
    void save() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(1);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/external/licence/save/licence_detail")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licenceDetail.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licenceDetail.getTillExternalId()))
                .param("token", token)).andExpect(status().isOk()).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        LicenceResponse licenceResponse = new LicenceResponse(licence, licenceDetail);

        String licenceNumber = result.getLicenceNumber();

        assertEquals(licenceNumber, aes.encrypt(licence.getLicenceNumber()).get());

        result.setLicenceNumber(aes.decrypt(licenceNumber).get());

        assertEquals(result.toString(), licenceResponse.toString());
    }

    @Test
    void saveWithoutEncoding() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(1);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/external/licence/save/licence_detail")
                .param("licenceNumber", licence.getLicenceNumber())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licenceDetail.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licenceDetail.getTillExternalId()))
                .param("token", token)).andExpect(status().is(404)).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        assertNotEquals(licence.getLicenceNumber(), result.getLicenceNumber());
        assertEquals(null, result.getLicenceNumber());
    }

    @Test
    void findIfLicenceNotActive() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licence.setActive(false);
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(1);
        licenceDetailRepository.save(licenceDetail);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licenceDetail.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licenceDetail.getTillExternalId()))
                .param("token", token)).andExpect(status().is(404)).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        assertEquals(result.toString(), new LicenceResponse().toString());
    }

    @Test
    void saveIfLicenceNotActive() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licence.setActive(false);
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(1);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/external/licence/save/licence_detail")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licenceDetail.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licenceDetail.getTillExternalId()))
                .param("token", token)).andExpect(status().is(404)).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        assertEquals(result.toString(), new LicenceResponse().toString());
    }
}