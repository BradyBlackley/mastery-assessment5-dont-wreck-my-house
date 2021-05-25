package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {
    @Override
    public List<Reservation> findByDate(LocalDate date) throws DataException {
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return null;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return false;
    }

    @Override
    public boolean deleteById(int reservationId) throws DataException {
        return false;
    }
}
