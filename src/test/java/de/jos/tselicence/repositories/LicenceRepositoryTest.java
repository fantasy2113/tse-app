package de.jos.tselicence.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.jos.tselicence.core.entities.Licence;
import de.jos.tselicence.core.util.DbUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import testutil.DbUtilsTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LicenceRepositoryTest {
    Licence defaultLicence;
    @Autowired
    LicenceRepository licenceRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        DbUtilsTest.drop();
        DbUtilsTest.create();
        defaultLicence = new Licence("1-1", "typ1", 1, 1, 1, LocalDateTime.now());
    }

    @Test
    void get() {
        assertFalse(licenceRepository.existsById(1));

        licenceRepository.save(defaultLicence);
        defaultLicence.setId(1);

        assertEquals(defaultLicence.toString(), licenceRepository.get(1).get().toString());
    }

    @Test
    void all() throws JsonProcessingException {
        List<Licence> licences = DbUtils.saveDemoLicencesToDbAndReturnAsList(licenceRepository);

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

        assertTrue(licenceRepository.existsById(1));

        assertTrue(licenceRepository.get(1).get().isActive());

        defaultLicence.setId(1);
        defaultLicence.setActive(false);
        defaultLicence.setLicenceNumber("ln");

        licenceRepository.update(defaultLicence);

        Licence licence = licenceRepository.get(1).get();

        assertFalse(licence.isActive());
        assertEquals("ln", licence.getLicenceNumber());
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.existsById(1));

        defaultLicence.setId(1);
        licenceRepository.delete(1);

        assertFalse(licenceRepository.existsById(1));
    }

    @Test
    void testDelete() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.existsById(1));

        defaultLicence.setId(1);
        licenceRepository.delete(defaultLicence);

        assertFalse(licenceRepository.existsById(1));
    }

    @Test
    void existsById() {
        licenceRepository.save(defaultLicence);

        assertTrue(licenceRepository.existsById(1));
    }

    @Test
    void findAllBy() {
        DbUtils.saveDemoLicencesToDbAndReturnAsList(licenceRepository);
        List<Licence> result = licenceRepository.filter(defaultLicence);
        defaultLicence.setId(1);
        assertEquals(1, result.size());
        assertEquals(defaultLicence.toString(), result.get(0).toString());
    }
}