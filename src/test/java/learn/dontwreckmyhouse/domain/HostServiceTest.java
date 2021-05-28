package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class HostServiceTest {

    HostService service;

    @BeforeEach
    void setup() {
        HostRepositoryDouble repository = new HostRepositoryDouble();
        service = new HostService(repository);
    }

    @Test
    void shouldFindAllHosts() throws DataException {
        assertNotNull(service.findAll());
        assertEquals(1, service.findAll().size());
    }

    @Test
    void shouldFindById() throws DataException {
        assertNotNull(service.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15"));
        assertEquals("Tester",
                service.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15").getLastName());
    }

    @Test
    void shouldFindByEmail() throws DataException {
        assertNotNull(service.findByEmail("thekardashians@gmail.com"));
        assertEquals("Tester", service.findByEmail("thekardashians@gmail.com").getLastName());
    }

    @Test
    void shouldAddValidHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertTrue(service.add(host).isSuccess());
        assertNotNull(service.add(host).getPayload().getHostId());
    }

    @Test
    void shouldNotAddDuplicateHost() throws DataException {
        Host host = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15", "Tester",
                "thekardashians@gmail.com", "(801) 1234567",
                "123 Main St", "Milwaukee", "WI",
                "12345", BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullLastNameHost() throws DataException {
        Host host = new Host(null, "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyLastNameHost() throws DataException {
        Host host = new Host("", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullEmailHost() throws DataException {
        Host host = new Host("NewHost", null, "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyEmailHost() throws DataException {
        Host host = new Host("NewHost", "", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullPhoneHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", null,
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddImproperlyFormattedPhone() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());

        host = new Host("NewHost", "newemail@email.com", "801 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());

        host = new Host("NewHost", "newemail@email.com", "8017654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());

        host = new Host("NewHost", "newemail@email.com", "801",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyPhoneHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullAddressHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                null, "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyAddressHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullCityHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", null, "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyCityHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullStateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", null, "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyStateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddImproperFormatStateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "Wisconsin", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullPostalHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", null,
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddEmptyPostalHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddImproperFormatPostalHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345-12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullStandardRateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                null, BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddNullWeekendRateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), null);
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddImproperFormatStandardRateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(-1), BigDecimal.valueOf(160));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldNotAddImproperFormatWeekendRateHost() throws DataException {
        Host host = new Host("NewHost", "newemail@email.com", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(0));
        assertFalse(service.add(host).isSuccess());
    }

    @Test
    void shouldUpdateFoundGuest() throws DataException {
        Host host = new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15", "Tester",
                "thekardashians@gmail.com", "(801) 1234567",
                "123 Main St", "Milwaukee", "WI",
                "12345", BigDecimal.valueOf(144), BigDecimal.valueOf(160));
        assertTrue(service.update(host).isSuccess());
    }

    @Test
    void shouldNotUpdateMissingGuest() throws DataException {
        Host host = new Host("1234", "NewHost", "nonexistent", "(801) 7654321",
                "321 Main St", "Milwaukee", "WI", "12345",
                BigDecimal.valueOf(144), BigDecimal.valueOf(-1));
        assertFalse(service.update(host).isSuccess());
    }

    @Test
    void shouldDeleteFoundGuest() throws DataException {
        assertTrue(service.deleteById("3edda6bc-ab95-49a8-8962-d50b53f84b15").isSuccess());
    }

    @Test
    void shouldNotDeleteMissingGuest() throws DataException {
        assertFalse(service.deleteById("1234").isSuccess());
    }
}
