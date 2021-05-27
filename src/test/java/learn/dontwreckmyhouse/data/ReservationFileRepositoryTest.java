package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH =
            "./data/test-data/test-reservations/2e72f86c-b8fe-4265-b4f1-304dea8762db-seed.csv";
    static final String TEST_FILE_PATH =
            "./data/test-data/test-reservations/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_DIR_PATH = "./data/test-data/test-reservations";

    private final GuestFileRepository guestRepository =
            new GuestFileRepository("./data/test-data/guests-test.csv");
    private final HostFileRepository hostRepository =
            new HostFileRepository("./data/test-data/hosts-test.csv");

    ReservationFileRepository repository =
            new ReservationFileRepository(TEST_DIR_PATH, guestRepository, hostRepository);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    void findReservationsByHostId() throws DataException {
        List<Reservation> actual =
                repository.findReservationsByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        assertNotNull(actual);
        assertEquals(12, actual.size());
    }

    @Test
    void findReservation() throws DataException {
        Reservation actual =
                repository.findReservation("2e72f86c-b8fe-4265-b4f1-304dea8762db", 1);
        assertNotNull(actual);
        assertEquals(LocalDate.of(2021,10,14), actual.getEndDate());
    }

    @Test
    void add() throws DataException {
        Guest guest = guestRepository.findById(1);
        Host host = hostRepository.findById("2e72f86c-b8fe-4265-b4f1-304dea8762db");
        Reservation reservation = new Reservation(guest, host);
        reservation.setStartDate(LocalDate.of(2020, 1, 1));
        reservation.setEndDate(LocalDate.of(2020, 1, 5));
        reservation.updateTotal();
        assertNotNull(repository.add(reservation));
        assertEquals(13, repository.findReservationsByHostId("2e72f86c-b8fe-4265-b4f1-304dea8762db").size());
    }

    @Test
    void update() throws DataException {
        Reservation reservation =
                repository.findReservation("2e72f86c-b8fe-4265-b4f1-304dea8762db", 1);
        reservation.setEndDate(LocalDate.of(2021,10,15));

        assertTrue(repository.update(reservation));

    }

}