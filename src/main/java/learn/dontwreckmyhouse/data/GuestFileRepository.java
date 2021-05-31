package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestFileRepository implements GuestRepository {

    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestFileRepository(@Value("${guestFilePath}")String directory) {
        this.filePath = directory;
    }

    @Override
    public List<Guest> findAll() throws DataException {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Guest findById(int id) throws DataException {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getGuestId() == id) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest findByEmail(String email) throws DataException {
        List<Guest> all = findAll();
        for (Guest guest : all) {
            if (guest.getEmail().equals(email)) {
                return guest;
            }
        }
        return null;
    }

    @Override
    public Guest add(Guest guest) throws DataException {
        List<Guest> all = findAll();
        int nextId = getNextId(all);
        guest.setGuestId(nextId);
        all.add(guest);
        writeToFile(all);
        return guest;
    }

    @Override
    public boolean update(Guest guest) throws DataException {
        List<Guest> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getGuestId() == guest.getGuestId()) {
                all.set(i, guest);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(int guestId) throws DataException {
        List<Guest> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getGuestId() == guestId) {
                all.remove(i);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    private int getNextId(List<Guest> guests) {
        int maxId = 0;
        for (Guest guest : guests) {
            if (maxId < guest.getGuestId()) {
                maxId = guest.getGuestId();
            }
        }
        return maxId + 1;
    }

    private void writeToFile(List<Guest> guests) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Guest guest : guests) {
                writer.println(serialize(guest));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Guest guest) {
        return String.format("%s,%s,%s,%s,%s,%s",
                guest.getGuestId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getState());
    }

    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setEmail(fields[3]);
        result.setPhone(fields[4]);
        result.setState(fields[5]);

        return result;
    }
}

