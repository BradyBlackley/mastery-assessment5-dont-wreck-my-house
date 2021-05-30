package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.HostRepository;
import learn.dontwreckmyhouse.models.Host;

import java.util.List;
import java.util.stream.Collectors;

public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() throws DataException {
        return repository.findAll();
    }

    public Host findById(String hostId) throws DataException {
        return repository.findById(hostId);
    }

    public Host findByEmail(String email) throws DataException {
        return repository.findByEmail(email);
    }

    public List<Host> findByLastName(String prefix) throws DataException {
        return repository.findAll().stream()
                .filter(h -> h.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Result<Host> add(Host host) throws DataException {
        Result<Host> result = validate(host);
        if (repository.findAll().stream()
                .anyMatch(h -> h.getEmail().equals(host.getEmail()))) {
            result.addErrorMessage("The given host already exists.");
        }
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(repository.add(host));
        return result;
    }

    public Result<Host> update(Host host) throws DataException {
        Result<Host> result = validate(host);

        if (host.getHostId().isEmpty() || host.getHostId().isBlank()) {
            result.addErrorMessage("Host `id` is required.");
        }

        if (result.isSuccess()) {
            if (repository.update(host)) {
                result.setPayload(host);
            } else {
                String message = String.format("Host id %s was not found.", host.getHostId());
                result.addErrorMessage(message);
            }
        }
        return result;
    }

    public Result<Host> deleteById(String hostId) throws DataException {
        Result<Host> result = new Result<>();
        if (!repository.deleteById(hostId)) {
            String message = String.format("Host id %s was not found.", hostId);
            result.addErrorMessage(message);
        }
        return result;
    }

    private Result<Host> validate(Host host) throws DataException {

        Result<Host> result = new Result<>();

        if (host == null) {
            result.addErrorMessage("Host cannot be null.");
            return result;
        }

        if (host.getLastName() == null || host.getLastName().isEmpty()) {
            result.addErrorMessage("Host last name is required.");
            return result;
        }

        if (host.getEmail() == null || host.getEmail().isEmpty()) {
            result.addErrorMessage("Host email is required.");
            return result;
        }

        if (host.getPhone() == null || host.getPhone().isEmpty()) {
            result.addErrorMessage("Host phone is required.");
            return result;
        }

        if (host.getPhone().length() != 13
                || host.getPhone().charAt(0) != '('
                || host.getPhone().charAt(4) != ')'
                || host.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Host phone format is incorrect. The format should be (###) #######");
            return result;
        }

        if (host.getAddress() == null || host.getAddress().isEmpty()) {
            result.addErrorMessage("Host address is required.");
            return result;
        }

        if (host.getCity() == null || host.getCity().isEmpty()) {
            result.addErrorMessage("Host city is required.");
            return result;
        }

        if (host.getState() == null || host.getState().isEmpty()) {
            result.addErrorMessage("Host state is required.");
            return result;
        }

        if (host.getState().length() != 2) {
            result.addErrorMessage("Host state format is incorrect. The format should be the two letter abbreviation.");
            return result;
        }

        if (host.getPostalCode() == null || host.getPostalCode().isEmpty()) {
            result.addErrorMessage("Host postal code is required.");
            return result;
        }

        if (host.getPostalCode().length() != 5) {
            result.addErrorMessage("Host postal code format is incorrect. Format should be #####");
            return result;
        }

        if (host.getStandardRate() == null) {
            result.addErrorMessage("Host standard rate is required.");
            return result;
        }

        if (Double.parseDouble(host.getStandardRate().toString()) <= 0) {
            result.addErrorMessage("Host standard rate must be greater than 0");
            return result;
        }

        if (host.getWeekendRate() == null) {
            result.addErrorMessage("Host weekend rate is required.");
            return result;
        }

        if (Double.parseDouble(host.getWeekendRate().toString()) <= 0) {
            result.addErrorMessage("Host weekend rate must be greater than 0");
            return result;
        }

        return result;
    }

}
