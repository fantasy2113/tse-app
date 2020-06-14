package de.jos.tselicence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jos.tselicence.controller.responeses.LicenceResponse;
import de.jos.tselicence.core.DbSetup;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.entities.LicenceDetail;
import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.repositories.ILicenceDetailRepository;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.security.Authenticator;
import de.jos.tselicence.security.UrlSaveAes;
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

import static org.junit.jupiter.api.Assertions.*;
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
    DbSetup dbSetup;
    @Autowired
    Environment env;
    @Autowired
    private ILicenceRepository licenceRepository;
    @Autowired
    private ILicenceDetailRepository licenceDetailRepository;
    @Autowired
    private Authenticator authenticator;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);
        DbUtilsTest.insertDefaultUser(env, userRepository);
        user = DbUtilsTest.getValidUser(userRepository);
        token = authenticator.getToken(user.getUsername(), user.getPassword()).get();
    }

    @Test
    void freeWithoutLicence() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(100);
        licence.setTseType("fiskaly");
        licence.setLicenceNumber("123456789");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/free")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType()))
                .andExpect(status().is(404)).andReturn();

        assertEquals(-1, Integer.valueOf(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void freeWithLicence() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(100);
        licence.setTseType("fiskaly");
        licence.setLicenceNumber("123456789");
        licenceRepository.save(licence);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/free")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType()))
                .andExpect(status().isOk()).andReturn();

        assertEquals(100, Integer.valueOf(mvcResult.getResponse().getContentAsString()));
    }

    @Test
    void findWithDetailLicence() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licenceRepository.save(licence);
        licence.setId(1);

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber("1");
        licenceDetail.setTillExternalId("1");
        licenceDetail.setLicenceId(1);
        licenceDetailRepository.save(licenceDetail);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", licenceDetail.getBranchNumber())
                .param("tillExternalId", licenceDetail.getTillExternalId()))
                .andExpect(status().isOk()).andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        LicenceResponse licenceResponse = new LicenceResponse(licence, licenceDetail);

        String licenceNumber = result.getLicenceNumber();

        assertEquals(licenceNumber, aes.encrypt(licence.getLicenceNumber()).get());

        result.setLicenceNumber(aes.decrypt(licenceNumber).get());

        assertEquals(result.toString(), licenceResponse.toString());
    }

    @Test
    void findWithoutDetailLicence() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
        licenceRepository.save(licence);

        mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", "1")
                .param("tillExternalId", "1"))
                .andExpect(status().is(204));
    }

    @Test
    void findWithoutLicenceAndDetailLicence() throws Exception {
        Licence licence = new Licence();
        licence.setNumberOfTse(1);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");

        mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", "1")
                .param("tillExternalId", "1"))
                .andExpect(status().is(404));
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
        licenceDetail.setBranchNumber("1");
        licenceDetail.setTillExternalId("1");
        licenceDetail.setLicenceId(1);
        licenceDetailRepository.save(licenceDetail);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", licence.getLicenceNumber())
                .param("tseType", licence.getTseType())
                .param("branchNumber", licenceDetail.getBranchNumber())
                .param("tillExternalId", licenceDetail.getTillExternalId()))
                .andExpect(status().is(404))
                .andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        assertNotEquals(licence.getLicenceNumber(), result.getLicenceNumber());
        assertNull(result.getLicenceNumber());
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
        licenceDetail.setBranchNumber("1");
        licenceDetail.setTillExternalId("1");
        licenceDetail.setLicenceId(1);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/external/licence/save/licence_detail")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", licenceDetail.getBranchNumber())
                .param("tillExternalId", licenceDetail.getTillExternalId()))
                .andExpect(status().isOk())
                .andReturn();

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
        licenceDetail.setBranchNumber("1");
        licenceDetail.setTillExternalId("1");
        licenceDetail.setLicenceId(1);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/external/licence/save/licence_detail")
                .param("licenceNumber", licence.getLicenceNumber())
                .param("tseType", licence.getTseType())
                .param("branchNumber", licenceDetail.getBranchNumber())
                .param("tillExternalId", licenceDetail.getTillExternalId()))
                .andExpect(status().is(404))
                .andReturn();

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
        licenceDetail.setBranchNumber("1");
        licenceDetail.setTillExternalId("1");
        licenceDetail.setLicenceId(1);
        licenceDetailRepository.save(licenceDetail);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/external/licence/find")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", licenceDetail.getBranchNumber())
                .param("tillExternalId", licenceDetail.getTillExternalId()))
                .andExpect(status().is(404))
                .andReturn();

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
        licenceDetail.setBranchNumber("1");
        licenceDetail.setTillExternalId("1");
        licenceDetail.setLicenceId(1);
        licenceDetail.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/external/licence/save/licence_detail")
                .param("licenceNumber", aes.encrypt(licence.getLicenceNumber()).get())
                .param("tseType", licence.getTseType())
                .param("branchNumber", licenceDetail.getBranchNumber())
                .param("tillExternalId", licenceDetail.getTillExternalId()))
                .andExpect(status().is(404))
                .andReturn();

        LicenceResponse result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LicenceResponse.class);

        assertEquals(result.toString(), new LicenceResponse().toString());
    }
}