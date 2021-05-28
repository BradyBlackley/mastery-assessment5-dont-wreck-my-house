package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    public final static Host HOST =
            new Host("3edda6bc-ab95-49a8-8962-d50b53f84b15", "Tester",
                    "thekardashians@gmail.com", "(801) 1234567",
                    "123 Main St", "Milwaukee", "WI",
                    "12345", BigDecimal.valueOf(144), BigDecimal.valueOf(160));
    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepositoryDouble() {
        hosts.add(HOST);
    }


    @Override
    public List<Host> findAll() throws DataException {
        return new ArrayList<>(hosts);
    }

    @Override
    public Host findById(String id) throws DataException {
        return findAll().stream()
                .filter(h -> h.getHostId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        return findAll().stream()
                .filter(h -> h.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host add(Host host) throws DataException {
        List<Host> all = findAll();
        host.setHostId(java.util.UUID.randomUUID().toString());
        all.add(host);
        return host;
    }

    @Override
    public boolean update(Host host) throws DataException {
        return findById(host.getHostId()) != null;
    }

    @Override
    public boolean deleteById(String hostId) throws DataException {
        return findById(hostId) != null;
    }
}
