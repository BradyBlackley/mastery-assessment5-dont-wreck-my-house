package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.util.List;

public interface HostRepository {

    List<Host> findAll() throws DataException;

    Host findByEmail(String email) throws DataException;

    Host add(Host host) throws DataException;

    boolean update(Host host) throws DataException;

    boolean deleteById(String hostId) throws DataException;
}
