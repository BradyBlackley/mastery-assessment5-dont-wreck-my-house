package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.util.List;

public class HostFileRepository implements HostRepository {

    @Override
    public List<Host> findAll() {
        return null;
    }

    @Override
    public Host findByEmail(String email) {
        return null;
    }

    @Override
    public Host add(Host host) throws DataException {
        return null;
    }

    @Override
    public boolean update(Host host) throws DataException {
        return false;
    }

    @Override
    public boolean deleteById(int hostId) throws DataException {
        return false;
    }
}
