package de.jos.tselicence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.repositories.ILicenceRepository;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.core.util.DbUtils;
import de.jos.tselicence.security.Authenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import testutil.DbUtilsTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LicenceControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    IUserRepository userRepository;
    User user;
    @Autowired
    private ILicenceRepository licenceRepository;
    @Autowired
    private Authenticator authenticator;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop();
        DbUtilsTest.create();
        DbUtils.insertAdmin(userRepository);
        user = DbUtilsTest.getValidUser(userRepository);
    }

    @Test
    void getShouldReturnOkAndLicence() throws Exception {
        Licence expected = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());
        licenceRepository.save(expected);
        expected.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/licence/{id}", "1")
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().isOk()).andReturn();

        Licence result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Licence.class);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    void getShouldReturnOkAndNoLicence() throws Exception {
        Licence licence = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());
        licenceRepository.save(licence);
        licence.setId(1);

        mvc.perform(MockMvcRequestBuilders.get("/licence/{id}", "5")
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().is(404));
    }

    @Test
    void allShouldReturnOkAndLicences() throws Exception {
        List<Licence> expected = DbUtils.saveDemoLicencesToDbAndReturnAsList(licenceRepository);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/licences")
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().isOk()).andReturn();

        assertEquals(objectMapper.writeValueAsString(expected), mvcResult.getResponse().getContentAsString());
    }

    @Test
    void findShouldReturnOkAndLicence() throws Exception {
        Licence licence = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());
        licenceRepository.save(licence);
        licence.setId(1);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/licence/find")
                .param("licenceNumber", licence.getLicenceNumber())
                .param("tseType", licence.getTseType())
                .param("branchNumber", String.valueOf(licence.getBranchNumber()))
                .param("tillExternalId", String.valueOf(licence.getTillExternalId()))
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().isOk()).andReturn();

        Licence result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Licence.class);

        assertEquals(licence.toString(), result.toString());
    }

    @Test
    void deleteShouldReturnOkAndDeleteLicence() throws Exception {
        Licence expected = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());

        licenceRepository.save(expected);

        assertTrue(licenceRepository.existsById(1));

        mvc.perform(MockMvcRequestBuilders.delete("/licence/delete/{id}", "1")
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().is(204));

        assertFalse(licenceRepository.existsById(1));
    }

    @Test
    void deleteWithEntityShouldReturnOkAndDeleteLicence() throws Exception {
        int licId = 1;
        Licence expected = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());

        licenceRepository.save(expected);
        expected.setId(licId);

        assertTrue(licenceRepository.existsById(licId));

        mvc.perform(MockMvcRequestBuilders.delete("/licence/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expected))
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().is(204));

        assertFalse(licenceRepository.existsById(1));
    }

    @Test
    void updateIfEntityExist() throws Exception {
        int id = 1;
        Licence expected = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());

        licenceRepository.save(expected);
        assertTrue(licenceRepository.existsById(id));

        Optional<Licence> optionalLicence = licenceRepository.get(id);
        assertTrue(optionalLicence.isPresent());
        expected = optionalLicence.get();
        assertTrue(expected.isActive());
        expected.setActive(false);

        mvc.perform(MockMvcRequestBuilders.put("/licence/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expected))
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().isOk());

        assertTrue(licenceRepository.existsById(1));
        assertFalse(expected.isActive());
    }

    @Test
    void updateIfEntityNotExist() throws Exception {
        final int id = 1;
        Licence licence = new Licence(id, "1-1", "typ1", 1, 1, 1, LocalDateTime.now());
        assertFalse(licenceRepository.existsById(id));

        mvc.perform(MockMvcRequestBuilders.put("/licence/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(licence))
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().is(404));

        assertFalse(licenceRepository.existsById(1));
    }

    @Test
    void saveIfEntityNotExist() throws Exception {
        int id = 1;
        Licence expected = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());

        assertFalse(licenceRepository.existsById(id));

        mvc.perform(MockMvcRequestBuilders.post("/licence/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expected))
                .header("htoken", authenticator.getToken(user.getUsername(), user.getPassword()).get())).andExpect(status().isOk());

        expected.setId(id);

        assertEquals(objectMapper.writeValueAsString(expected), objectMapper.writeValueAsString(licenceRepository.get(id).get()));
    }
}