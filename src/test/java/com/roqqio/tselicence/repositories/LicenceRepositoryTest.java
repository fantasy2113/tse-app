package com.roqqio.tselicence.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roqqio.tselicence.core.DbSetup;
import com.roqqio.tselicence.core.entities.Licence;
import com.roqqio.tselicence.core.entities.LicenceDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import testutil.DbUtilsTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LicenceRepositoryTest {
    Licence defaultLicence;
    @Autowired
    LicenceRepository licenceRepository;
    @Autowired
    LicenceDetailRepository licenceDetailRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DbSetup dbSetup;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop(dbSetup);
        DbUtilsTest.create(dbSetup);
        defaultLicence = new Licence();
        defaultLicence.setNumberOfTse(0);
        defaultLicence.setTseType("typ1");
        defaultLicence.setLicenceNumber("1-1");
    }

    @Test
    void get() {
        assertFalse(licenceRepository.exists(1));

        licenceRepository.save(defaultLicence);
        defaultLicence.setId(1);

        assertEquals(defaultLicence.toString(), licenceRepository.get(1).get().toString());
    }

    @Test
    void all() throws JsonProcessingException {
        List<Licence> licences = DbUtilsTest.saveDemoLicencesToDbAndReturnAsList(licenceRepository);

        assertEquals(objectMapper.writeValueAsString(licences), objectMapper.writeValueAsString(licenceRepository.all()));
    }

    @Test
    void find() {
        licenceRepository.save(defaultLicence);
        defaultLicence.setId(1);

        assertEquals(defaultLicence.toString(), licenceRepository.find(defaultLicence).get().toString());
    }

    @Test
    void update() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.exists(1));

        assertEquals(licenceRepository.get(1).get().getTseType(), "typ1");

        defaultLicence.setId(1);
        defaultLicence.setTseType("typ1_");
        defaultLicence.setLicenceNumber("ln");

        licenceRepository.update(defaultLicence);

        Licence licence = licenceRepository.get(1).get();

        assertEquals("ln", licence.getLicenceNumber());
        assertEquals("typ1_", licence.getTseType());
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.exists(1));

        defaultLicence.setId(1);
        licenceRepository.delete(1);

        assertFalse(licenceRepository.exists(1));
    }

    @Test
    void testDelete() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.exists(1));

        defaultLicence.setId(1);
        licenceRepository.delete(defaultLicence);

        assertFalse(licenceRepository.exists(1));
    }

    @Test
    void existsById() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.exists(1));
    }

    @Test
    void findAllBy() {
        DbUtilsTest.saveDemoLicencesToDbAndReturnAsList(licenceRepository);
        List<Licence> result = licenceRepository.filter(defaultLicence);
        defaultLicence.setId(1);

        assertEquals(1, result.size());
        assertEquals(defaultLicence.toString(), result.get(0).toString());
    }

    @Test
    void testTseAvailable() {
        long id = 1;

        Licence savedLicence = licenceRepository.save(defaultLicence).get();

        defaultLicence.setId(id);

        assertEquals(0, savedLicence.getTseAvailable());
        assertEquals(0, savedLicence.getNumberOfTse());
        assertEquals(0, licenceRepository.get(id).get().getTseAvailable());
        assertEquals(0, licenceRepository.get(id).get().getNumberOfTse());

        LicenceDetail licenceDetail = new LicenceDetail();
        licenceDetail.setBranchNumber(1);
        licenceDetail.setTillExternalId(1);
        licenceDetail.setLicenceId(id);

        licenceDetailRepository.save(licenceDetail);

        assertEquals(-1, licenceRepository.get(id).get().getTseAvailable());


        defaultLicence.setNumberOfTse(3);

        assertEquals(2, licenceRepository.update(defaultLicence).get().getTseAvailable());

        licenceDetail.setBranchNumber(45);
        licenceDetail.setTillExternalId(3);

        licenceDetailRepository.save(licenceDetail);

        assertEquals(1, licenceRepository.get(id).get().getTseAvailable());

        defaultLicence.setNumberOfTse(2);

        assertEquals(0, licenceRepository.update(defaultLicence).get().getTseAvailable());
    }
}