package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;

import java.io.FileNotFoundException;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findReservationsByHostId(String hostId) throws DataException, FileNotFoundException;

    Reservation findReservation(String hostId, int reservationId) throws DataException, FileNotFoundException;

    Reservation add(Reservation reservation) throws DataException, FileNotFoundException;

    boolean update(Reservation reservation) throws DataException, FileNotFoundException;

    boolean deleteById(String hostId, int reservationId) throws DataException, FileNotFoundException;

}
