package learn.dontwreckmyhouse.domain;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.data.GuestRepository;
import learn.dontwreckmyhouse.models.Guest;

import java.util.List;

public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() throws DataException {
        return repository.findAll();
    }

    public Result<Guest> add(Guest guest) throws DataException {
        Result<Guest> result = validate(guest);
        if (!result.isSuccess()) {
            return result;
        }
        result.setPayload(repository.add(guest));
        return result;
    }

    private Result<Guest> validate(Guest guest) throws DataException {

        Result<Guest> result = new Result<>();

        if (guest == null) {
            result.addErrorMessage("Guest cannot be null.");
            return result;
        }

        if (guest.getFirstName() == null || guest.getFirstName().isBlank()) {
            result.addErrorMessage("Guest first name is required.");
            return result;
        }

        if (guest.getLastName() == null || guest.getLastName().isBlank()) {
            result.addErrorMessage("Guest last name is required.");
            return result;
        }

        if (guest.getEmail() == null || guest.getEmail().isBlank()) {
            result.addErrorMessage("Guest email is required.");
            return result;
        }

        if (guest.getPhone() == null || guest.getPhone().isBlank()) {
            result.addErrorMessage("Guest phone is required.");
            return result;
        }

        if(guest.getPhone().length() != 13
                && guest.getPhone().charAt(0) != '('
                && guest.getPhone().charAt(4) != ')'
                && guest.getPhone().charAt(5) != ' ') {
            result.addErrorMessage("Guest phone format is incorrect. The format should be (###) #######");
            return result;
        }

        if (guest.getState() == null || guest.getState().isBlank()) {
            result.addErrorMessage("Guest state is required.");
            return result;
        }

        if (guest.getState().length() != 2) {
            result.addErrorMessage("Guest state format is incorrect. The format should be the two letter abbreviation.");
            return result;
        }

        if (repository.findAll().stream()
                .anyMatch(g -> g.getEmail().equals(guest.getEmail()))) {
            result.addErrorMessage("The given guest already exists.");
        }
        return result;
    }

}
