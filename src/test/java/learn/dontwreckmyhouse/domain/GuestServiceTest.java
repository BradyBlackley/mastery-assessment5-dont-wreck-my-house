package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service;

    @BeforeEach
    void setup() {
        GuestRepositoryDouble repository = new GuestRepositoryDouble();
        service = new GuestService(repository);
    }

    @Test
    void shouldFindAllGuests() throws DataException {
        assertNotNull(service.findAll());
        assertEquals(1, service.findAll().size());
    }

    @Test
    void shouldAddValidGuest() throws DataException {
        Guest guest = new Guest(2, "Test", "Test",
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertTrue(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddNullName() throws DataException {
        Guest guest = new Guest(2, null, "Test",
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddBlankName() throws DataException {
        Guest guest = new Guest(2, "", "Test",
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddNullLastName() throws DataException {
        Guest guest = new Guest(2, "Test", null,
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddEmptyLastName() throws DataException {
        Guest guest = new Guest(2, "Test", "",
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddNullPhone() throws DataException {
        Guest guest = new Guest(2, "Test", "",
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddEmptyPhone() throws DataException {
        Guest guest = new Guest(2, "Test", null,
                "testingemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddImproperlyFormattedPhone() throws DataException {
        Guest guest = new Guest(2, "Test", null,
                "testingemail@gmail.com", "(801 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());

        guest = new Guest(2, "Test", null,
                "testingemail@gmail.com", "801 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());

        guest = new Guest(2, "Test", null,
                "testingemail@gmail.com", "8011234567", "WI");
        assertFalse(service.add(guest).isSuccess());

        guest = new Guest(2, "Test", null,
                "testingemail@gmail.com", "801", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddNullState() throws DataException {
        Guest guest = new Guest(2, "Test", "Test",
                "testingemail@gmail.com", "(801) 1234567", null);
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddEmptyState() throws DataException {
        Guest guest = new Guest(2, "Test", "Test",
                "testingemail@gmail.com", "(801) 1234567", "");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddImproperFormatState() throws DataException {
        Guest guest = new Guest(2, "Test", "Test",
                "testingemail@gmail.com", "(801) 1234567", "Wisconsin");
        assertFalse(service.add(guest).isSuccess());
    }

    @Test
    void shouldNotAddDuplicateGuest() throws DataException {
        Guest guest = new Guest(1, "Chester", "Tester",
                "fakeemail@gmail.com", "(801) 1234567", "WI");
        assertFalse(service.add(guest).isSuccess());
    }

}