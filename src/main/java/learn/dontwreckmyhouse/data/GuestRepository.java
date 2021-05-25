package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;

import java.util.List;

public interface GuestRepository {

    List<Guest> findAll() throws DataException;

    Guest findByEmail(String email) throws DataException;

    Guest add(Guest guest) throws DataException;

    boolean update(Guest guest) throws DataException;

    boolean deleteById(int guestId) throws DataException;

}
