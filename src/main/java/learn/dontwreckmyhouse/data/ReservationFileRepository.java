package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Reservation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationFileRepository implements ReservationRepository {

    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;
    private final GuestFileRepository guestRepository;
    private final HostFileRepository hostRepository;

    public ReservationFileRepository(@Value("${reservationFilePath}")String directory,
                                     GuestFileRepository guestRepository,
                                     HostFileRepository hostRepository) {
        this.directory = directory;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    @Override
    public List<Reservation> findReservationsByHostId(String hostId) throws DataException {
        ArrayList<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {
            reader.readLine(); //read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    reservations.add(deserialize(fields, hostId));
                }
            }
        } catch (IOException ex) {
            //don't throw on read
        }
        return reservations;
    }

    @Override
    public Reservation findReservation(String hostId, int reservationId) throws DataException {
        List<Reservation> reservations = findReservationsByHostId(hostId);
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all =
                findReservationsByHostId(reservation.getHost().getHostId());
        reservation.setReservationId(getNextId(all));
        all.add(reservation);
        writeToFile(all, reservation.getHost().getHostId());
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findReservationsByHostId(reservation.getHost().getHostId());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservation.getReservationId()) {
                all.set(i, reservation);
                writeToFile(all, reservation.getHost().getHostId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(String hostId, int reservationId) throws DataException {
        List<Reservation> all = findReservationsByHostId(hostId);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getReservationId() == reservationId) {
                all.remove(i);
                writeToFile(all, hostId);
                return true;
            }
        }
        return false;
    }

    private int getNextId(List<Reservation> reservations) {
        int maxId = 0;
        for (Reservation reservation : reservations) {
            if (maxId < reservation.getReservationId()) {
                maxId = reservation.getReservationId();
            }
        }
        return maxId + 1;
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getReservationId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields, String hostId) throws DataException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(Integer.parseInt(fields[0]));
        reservation.setStartDate(LocalDate.parse(fields[1]));
        reservation.setEndDate(LocalDate.parse(fields[2]));
        reservation.setGuest(guestRepository.findById(Integer.parseInt(fields[3])));
        reservation.setHost(hostRepository.findById(hostId));
        reservation.setTotal(new BigDecimal(fields[4]).setScale(2, RoundingMode.HALF_EVEN));
        return reservation;
    }

    private void writeToFile(List<Reservation> reservations, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);
            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }
}
