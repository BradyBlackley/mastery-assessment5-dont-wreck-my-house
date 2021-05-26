package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/test-data/hosts-seed.csv";
    static final String TEST_FILE_PATH = "./data/test-data/hosts-test.csv";

    HostFileRepository repository = new HostFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setUpTest() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findAll() throws DataException {
        List<Host> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(5, actual.size());
    }

    @Test
    void shouldNotFindByMissingEmail() throws DataException {
        Host actual = repository.findByEmail("nonExistentEmail@gmail.com");
        assertNull(actual);
    }

    @Test
    void shouldFindByFoundEmail() throws DataException {
        Host actual = repository.findByEmail("eyearnes0@sfgate.com");
        assertEquals("Yearnes", actual.getLastName());
    }

    @Test
    void shouldNotfindByMissingId() throws DataException {
        Host actual = repository.findById("12341234123412341234");
        assertNull(actual);
    }

    @Test
    void shouldFindByFoundId() throws DataException {
        Host actual = repository.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(actual);
        assertEquals("Yearnes", actual.getLastName());
    }

    @Test
    void add() throws DataException {
        Host actual = new Host();
        actual.setLastName("Blackley");
        actual.setEmail("email@gmail.com");
        actual.setPhone("(801) 1234567");
        actual.setAddress("123 Main St");
        actual.setCity("Test");
        actual.setState("AZ");
        actual.setPostalCode("12345");
        actual.setStandardRate(BigDecimal.valueOf(433));
        actual.setWeekendRate(BigDecimal.valueOf(541.25));
        assertNotNull(repository.add(actual));
        List<Host> all = repository.findAll();
        assertEquals(6, all.size());
    }

    @Test
    void update() throws DataException {
        Host host = repository.findByEmail("eyearnes0@sfgate.com");
        host.setLastName("NoLongerRhodes");
        assertTrue(repository.update(host));
        assertEquals("NoLongerRhodes", repository.findByEmail("eyearnes0@sfgate.com").getLastName());
    }

    @Test
    void delete() throws DataException {
        String hostId = repository.findByEmail("eyearnes0@sfgate.com").getHostId();
        assertTrue(repository.deleteById(hostId));
        assertEquals(4, repository.findAll().size());
    }

}