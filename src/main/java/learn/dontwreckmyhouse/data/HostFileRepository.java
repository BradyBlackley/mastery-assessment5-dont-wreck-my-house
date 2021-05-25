package learn.dontwreckmyhouse.data;

import learn.dontwreckmyhouse.models.Host;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HostFileRepository implements HostRepository {

    private static final String HEADER =
            "id,last_name,email,phone,address,city,state,postal_code,standard_rate,weekend_rate";
    private final String filePath;

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Host> findAll() throws DataException {
        ArrayList<Host> hosts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();//read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 10) {
                    hosts.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            //don't throw on read
        }
        return hosts;
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        List<Host> all = findAll();
        for (Host host : all) {
            if (host.getEmail().equals(email)) {
                return host;
            }
        }
        return null;
    }

    @Override
    public Host add(Host host) throws DataException {
        List<Host> all = findAll();
        host.setHostId(java.util.UUID.randomUUID().toString());
        all.add(host);
        writeToFile(all);
        return host;
    }

    @Override
    public boolean update(Host host) throws DataException {
        List<Host> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getHostId().equals(host.getHostId())) {
                all.set(i, host);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteById(String hostId) throws DataException {
        List<Host> all = findAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getHostId().equals(hostId)) {
                all.remove(i);
                writeToFile(all);
                return true;
            }
        }
        return false;
    }

    private void writeToFile(List<Host> hosts) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);
            for (Host host : hosts) {
                writer.println(serialize(host));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Host host) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                host.getHostId(),
                host.getLastName(),
                host.getEmail(),
                host.getPhone(),
                host.getAddress(),
                host.getCity(),
                host.getState(),
                host.getPostalCode(),
                host.getStandardRate(),
                host.getWeekendRate());
    }

    private Host deserialize(String[] fields) {
        Host result = new Host();
        result.setHostId(fields[0]);
        result.setLastName(fields[1]);
        result.setEmail(fields[2]);
        result.setPhone(fields[3]);
        result.setAddress(fields[4]);
        result.setCity(fields[5]);
        result.setState(fields[6]);
        result.setPostalCode(fields[7]);
        result.setStandardRate(Double.parseDouble(fields[8]));
        result.setWeekendRate(Double.parseDouble(fields[9]));
        return result;
    }
}
