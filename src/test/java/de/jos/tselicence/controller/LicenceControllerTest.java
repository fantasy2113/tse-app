package de.jos.tselicence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jos.tselicence.core.DbSetup;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.security.Authenticator;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LicenceControllerTest {
    @Autowired
    Authenticator authenticator;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    ILicenceRepository licenceRepository;
    @Autowired
    DbSetup dbSetup;
    @Autowired
    Environment env;

    Licence licence;
    User user;
    Cookie cookie;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);
        DbUtilsTest.insertDefaultUser(env, userRepository);
        user = DbUtilsTest.getValidUser(userRepository);
        cookie = new Cookie("ctoken", authenticator.getToken(user.getUsername(), user.getPassword()).get());
        licence = new Licence();
        licence.setNumberOfTse(0);
        licence.setTseType("typ1");
        licence.setLicenceNumber("1-1");
    }

    @Test
    void getShouldReturnOkAndLicence() throws Exception {
        licenceRepository.save(licence);
        licence.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/internal/licence/{id}", "1")
                .cookie(cookie)).andExpect(status().isOk()).andReturn();

        Licence result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Licence.class);

        assertEquals(licence.toString(), result.toString());
    }

    @Test
    void getWithCookieShouldReturn401() throws Exception {
        cookie = new Cookie("ctoken", "notvalidtoken");
        licenceRepository.save(licence);
        licence.setId(1);

        mvc.perform(MockMvcRequestBuilders.get("/internal/licence/{id}", "1")
                .cookie(cookie)).andExpect(status().is(401));
    }

    @Test
    void getWithoutCookieShouldReturn401() throws Exception {
        licenceRepository.save(licence);
        licence.setId(1);

        mvc.perform(MockMvcRequestBuilders.get("/internal/licence/{id}", "1")).andExpect(status().is(401));
    }

    @Test
    void getShouldReturnOkAndNoLicence() throws Exception {
        licenceRepository.save(licence);
        licence.setId(1);

        mvc.perform(MockMvcRequestBuilders.get("/internal/licence/{id}", "5")
                .cookie(cookie)).andExpect(status().is(404));
    }

    @Test
    void allShouldReturnOkAndLicences() throws Exception {
        List<Licence> expected = DbUtilsTest.saveDemoLicencesToDbAndReturnAsList(licenceRepository);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/internal/licences")
                .cookie(cookie)).andExpect(status().isOk()).andReturn();

        assertEquals(objectMapper.writeValueAsString(expected), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void filter() throws Exception {
        licenceRepository.save(licence);
        licence.setId(1);
        List<Licence> expected = new ArrayList<>();
        expected.add(licence);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put("/internal/licence/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licence))
                .cookie(cookie)).andExpect(status().isOk()).andReturn();

        assertEquals(objectMapper.writeValueAsString(expected), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void deleteShouldReturnOkAndDeleteLicence() throws Exception {

        licenceRepository.save(licence);

        assertTrue(licenceRepository.exists(1));

        mvc.perform(MockMvcRequestBuilders.delete("/internal/licence/delete/{id}", "1")
                .cookie(cookie)).andExpect(status().is(204));

        assertFalse(licenceRepository.exists(1));
    }

    @Test
    void deleteWithEntityShouldReturnOkAndDeleteLicence() throws Exception {
        int licId = 1;

        licenceRepository.save(licence);
        licence.setId(licId);

        assertTrue(licenceRepository.exists(licId));

        mvc.perform(MockMvcRequestBuilders.delete("/internal/licence/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licence))
                .cookie(cookie)).andExpect(status().is(204));

        assertFalse(licenceRepository.exists(1));
    }

    @Test
    void updateIfEntityExist() throws Exception {
        int id = 1;

        licenceRepository.save(licence);
        assertTrue(licenceRepository.exists(id));

        Optional<Licence> optionalLicence = licenceRepository.get(id);
        assertTrue(optionalLicence.isPresent());
        licence = optionalLicence.get();
        assertEquals("typ1", licence.getTseType());
        licence.setTseType("typ1_");

        mvc.perform(MockMvcRequestBuilders.put("/internal/licence/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licence))
                .cookie(cookie)).andExpect(status().isOk());

        assertTrue(licenceRepository.exists(1));
        assertEquals("typ1_", licence.getTseType());
    }

    @Test
    void updateIfEntityNotExist() throws Exception {
        final int id = 1;
        assertFalse(licenceRepository.exists(id));

        mvc.perform(MockMvcRequestBuilders.put("/internal/licence/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licence))
                .cookie(cookie)).andExpect(status().is(404));

        assertFalse(licenceRepository.exists(1));
    }

    @Test
    void saveIfEntityNotExist() throws Exception {
        int id = 1;

        assertFalse(licenceRepository.exists(id));

        mvc.perform(MockMvcRequestBuilders.post("/internal/licence/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licence))
                .cookie(cookie)).andExpect(status().isOk());

        licence.setId(id);

        assertEquals(objectMapper.writeValueAsString(licence), objectMapper.writeValueAsString(licenceRepository.get(id).get()));
    }
}