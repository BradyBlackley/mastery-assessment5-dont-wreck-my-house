package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.data.ReservationRepository;
import learn.dontwreckmyhouse.models.Reservation;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              GuestRepository guestRepository,
                              HostRepository hostRepository) {
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    public List<Reservation> findReservationsByHostId(String hostId) throws DataException, FileNotFoundException {
        return reservationRepository.findReservationsByHostId(hostId);
    }

    public Reservation findReservation(String hostId, int reservationId) throws DataException, FileNotFoundException {
        return reservationRepository.findReservation(hostId, reservationId);
    }

    public Result<Reservation> add(Reservation reservation) throws DataException, FileNotFoundException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(reservationRepository.add(reservation));
        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException, FileNotFoundException {
        Result<Reservation> result = validate(reservation);
        if (reservation.getReservationId() <= 0) {
            result.addErrorMessage("Reservation `id` is required.");
        }
        if (result.isSuccess()) {
            if (reservationRepository.update(reservation)) {
                result.setPayload(reservation);
            } else {
                String message = String.format("Reservation id %s was not found.", reservation.getReservationId());
                result.addErrorMessage(message);
            }
        }
        return result;
    }

    public Result<Reservation> deleteById(String hostId, int reservationId) throws DataException, FileNotFoundException {
        Result<Reservation> result = new Result<>();
        if (!reservationRepository.deleteById(hostId, reservationId)) {
            String message = String.format("Reservation id %s or host id %s was not found.", reservationId, hostId);
            result.addErrorMessage(message);
        }
        return result;
    }

    private Result<Reservation> validate(Reservation reservation) throws DataException, FileNotFoundException {
        Result<Reservation> result = validateNulls(reservation);

        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Reservation cannot be null.");
            return result;
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Reservation start date cannot be null.");
            return result;
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("Reservation end date cannot be null.");
            return result;
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Reservation guest cannot be null.");
            return result;
        }

        if (reservation.getHost() == null) {
            result.addErrorMessage("Reservation host cannot be null.");
            return result;
        }

        if (reservation.getTotal() == null) {
            result.addErrorMessage("Reservation total cannot be null");
            return result;
        }
        return result;
    }

    private void validateFields(Reservation reservation, Result<Reservation> result) throws DataException, FileNotFoundException {

        if (reservation.getStartDate().isBefore(LocalDate.now()) ||
                reservation.getEndDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Reservation cannot be booked in the past");
        }

        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Reservation start date cannot be after the end date");
        }

        if (
                reservationRepository.findReservationsByHostId(reservation.getHost().getHostId()).stream()
                        .anyMatch(r -> r.getEndDate().isAfter(reservation.getStartDate()))) {
            result.addErrorMessage("Reservation conflicts with an existing reservation");
        }

        if (
                reservationRepository.findReservationsByHostId(reservation.getHost().getHostId()).stream()
                        .anyMatch(r -> r.getStartDate().equals(reservation.getStartDate()))) {
            result.addErrorMessage("Reservation conflicts with an existing reservation");
        }
    }

    private void validateChildrenExist(Reservation reservation, Result<Reservation> result) throws DataException {

        if (guestRepository.findById(reservation.getGuest().getGuestId()) == null) {
            result.addErrorMessage("Reservation guest does not exist");
        }

        if (hostRepository.findById(reservation.getHost().getHostId()) == null) {
            result.addErrorMessage("Reservation host does not exist");
        }
    }

}
