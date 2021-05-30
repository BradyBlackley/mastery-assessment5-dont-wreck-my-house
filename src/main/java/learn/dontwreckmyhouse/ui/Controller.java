package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.io.FileNotFoundException;
import java.util.List;

public class Controller {

    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService,
                      ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House!");
        try {
            runAppLoop();
        } catch (DataException | FileNotFoundException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException, FileNotFoundException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservationsForHost();
                    break;
                case MAKE_A_RESERVATION:
                    makeAReservation();
                    break;
                case EDIT_A_RESERVATION:
                    editAReservation();
                    break;
                case CANCEL_A_RESERVATION:
                    cancelAReservation();
                    break;

            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsForHost() throws DataException, FileNotFoundException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        //TODO: let user choose to find host by email or last name
        int selection = view.selectViewReservationsForHostOption();
        List<Reservation> reservations = null;
        Host host = null;
        switch (selection) {
            case 1 :
                host = view.chooseHostByEmail(hostService.findAll());
                reservations = reservationService.findReservationsByHostId(host.getHostId());
                break;
            case 2 :
                break;
        }
        view.displayReservations(reservations, host);
        view.enterToContinue();
    }

    private void makeAReservation() throws DataException {

    }

    private void editAReservation() throws DataException {

    }

    private void cancelAReservation() throws DataException {

    }


}
