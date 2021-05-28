package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    public final static Guest GUEST =
            new Guest(1, "Chester", "Tester",
                    "fakeemail@gmail.com", "(801) 1234567", "WI");

    public final static Host HOST =
            new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15", "Tester",
                    "thekardashians@gmail.com", "(801) 1234567",
                    "123 Main St", "Milwaukee", "WI",
                    "12345", BigDecimal.valueOf(144), BigDecimal.valueOf(160));

    public final static Reservation RESERVATION =
            new Reservation(1, LocalDate.now(), LocalDate.now().plusDays(3), GUEST, HOST);

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble(){
        reservations.add(RESERVATION);
    }

    @Override
    public List<Reservation> findReservationsByHostId(String hostId) throws DataException, FileNotFoundException {
        return reservations.stream()
                .filter(r -> r.getHost().getHostId().equals(hostId))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation findReservation(String hostId, int reservationId) throws DataException, FileNotFoundException {
        return findReservationsByHostId(hostId).stream()
                .filter(r -> r.getReservationId() == reservationId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException, FileNotFoundException {
        List<Reservation> all = findReservationsByHostId(reservation.getHost().getHostId());
        int nextId = all.stream()
                .mapToInt(Reservation::getReservationId)
                .max()
                .orElse(0) + 1;
        reservation.setReservationId(nextId);
        all.add(reservation);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException, FileNotFoundException {
        return findReservation(reservation.getHost().getHostId(), reservation.getReservationId()) != null;
    }

    @Override
    public boolean deleteById(String hostId, int reservationId) throws DataException, FileNotFoundException {
        return findReservation(hostId, reservationId) != null;
    }
}
