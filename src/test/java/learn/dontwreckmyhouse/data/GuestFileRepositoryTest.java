package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/test-data/guests-seed.csv";
    static final String TEST_FILE_PATH = "./data/test-data/guests-test.csv";

    GuestFileRepository repository = new GuestFileRepository(TEST_FILE_PATH);

    @BeforeEach
    void setUpTest() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findAll() throws DataException {
        List<Guest> actual = repository.findAll();
        assertEquals(5,actual.size());
    }

    @Test
    void findByEmail() throws DataException {
        Guest actual = repository.findByEmail("nonExistentEmail@gmail.com");
        assertNull(actual);
        actual = repository.findByEmail("slomas0@mediafire.com");
        assertEquals("Sullivan", actual.getFirstName());
    }

    @Test
    void add() throws DataException {
        Guest actual = new Guest();
        actual.setFirstName("Brady");
        actual.setLastName("Blackley");
        actual.setPhone("8016906097");
        actual.setEmail("email@gmail.ccom");
        actual.setState("WI");
        assertNotNull(repository.add(actual));
        List<Guest> all = repository.findAll();
        assertEquals(6, all.size());
    }

    @Test
    void update() throws DataException {
        Guest guest = repository.findByEmail("lgueny3@example.com");
        guest.setFirstName("Leo");
        assertTrue(repository.update(guest));
        assertEquals("Leo", repository.findByEmail("lgueny3@example.com").getFirstName());
    }

    @Test
    void delete() throws DataException {
        int guestId = repository.findByEmail("lgueny3@example.com").getGuestId();
        assertTrue(repository.deleteById(guestId));
        assertEquals(4, repository.findAll().size());
    }
}