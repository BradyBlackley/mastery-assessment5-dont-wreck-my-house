package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepositoryDouble;
import learn.dontwreckmyhouse.data.HostRepositoryDouble;
import learn.dontwreckmyhouse.data.ReservationRepositoryDouble;
import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new ReservationRepositoryDouble(),
            new GuestRepositoryDouble(),
            new HostRepositoryDouble());

    @Test
    void shouldFindReservationsByHostId() throws DataException, FileNotFoundException {
        assertNotNull(service.findReservationsByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15"));
        assertEquals(1, service.findReservationsByHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15").size());
    }

    @Test
    void shouldFindReservation() throws DataException, FileNotFoundException {
        assertNotNull(service.findReservation("3edda6bc-ab95-49a8-8962-d50b53f84b15", 1));
        assertEquals("Chester", service.findReservation("3edda6bc-ab95-49a8-8962-d50b53f84b15", 1).getGuest().getFirstName());
    }

    @Test
    void shouldAdd() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(20));
        reservation.setEndDate(LocalDate.now().plusDays(25));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        reservation.updateTotal();
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNullGuest() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        reservation.setGuest(null);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        reservation.updateTotal();
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddMissingGuest() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        reservation.setGuest(new Guest());
        reservation.setHost(ReservationRepositoryDouble.HOST);
        reservation.updateTotal();
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullHost() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(null);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddMissingHost() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(new Host());
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddConflictingReservationDate() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        reservation.updateTotal();
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddConflictingReservationDate2() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(2));
        reservation.setEndDate(LocalDate.now().plusDays(5));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        reservation.updateTotal();
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddReservationStartDateFromThePast() throws DataException, FileNotFoundException {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().minusDays(5));
        reservation.setEndDate(LocalDate.now().plusDays(5));
        reservation.setGuest(ReservationRepositoryDouble.GUEST);
        reservation.setHost(ReservationRepositoryDouble.HOST);
        reservation.updateTotal();
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }
}
