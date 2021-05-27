package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    public final static Guest GUEST =
            new Guest(1, "Chester", "Tester",
                    "fakeemail@gmail.com", "(801) 1234567", "WI");
    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepositoryDouble() {
        guests.add(GUEST);
    }

    @Override
    public List<Guest> findAll() throws DataException {
        return new ArrayList<>(guests);
    }

    @Override
    public Guest findById(int id) throws DataException {
        return findAll().stream()
                .filter(g -> g.getGuestId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByEmail(String email) throws DataException {
        return findAll().stream()
                .filter(g -> g.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        List<Guest> all = findAll();

        int nextId = all.stream()
                .mapToInt(Guest::getGuestId)
                .max()
                .orElse(0) + 1;

        guest.setGuestId(nextId);

        all.add(guest);
        return guest;

    }

    @Override
    public boolean update(Guest guest) throws DataException {
        return findById(guest.getGuestId()) != null;
    }

    @Override
    public boolean deleteById(int guestId) throws DataException {
        return findById(guestId) != null;
    }
}
