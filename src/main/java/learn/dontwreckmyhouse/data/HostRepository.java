package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;

import java.io.FileNotFoundException;
import java.util.List;

public interface HostRepository {

    List<Host> findAll() throws DataException;

    Host findByEmail(String email);

    Host add(Host host) throws DataException;

    boolean update(Host host) throws DataException;

    boolean deleteById(int hostId) throws DataException;
}
