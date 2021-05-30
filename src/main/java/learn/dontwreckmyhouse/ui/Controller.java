package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.data.DataException;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.domain.Result;
import learn.dontwreckmyhouse.models.Guest;
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
        List<Reservation> reservations = null;
        Host host = selectHost();
        if (host != null) {
            reservations = reservationService.findReservationsByHostId(host.getHostId());
        }
        view.displayReservations(reservations, host);
        view.enterToContinue();
    }

    private void makeAReservation() throws DataException, FileNotFoundException {
        view.displayHeader(MainMenuOption.MAKE_A_RESERVATION.getMessage());
        Guest guest = selectGuest();
        if (guest == null) {
            return;
        }
        Host host = selectHost();
        if (host == null) {
            return;
        }
        List<Reservation> reservations = null;
        reservations = reservationService.findReservationsByHostId(host.getHostId());

        view.displayReservations(reservations, host);
        Reservation reservation = view.makeReservation(host, guest);
        view.displayReservationSummary(reservation);
        if (view.isOkay()) {
            Result<Reservation> result = reservationService.add(reservation);
            if (!result.isSuccess()) {
                view.displayStatus(false, result.getErrorMessages());
            } else {
                String successMessage = String.format("Reservation %s created.", result.getPayload().getReservationId());
                view.displayStatus(true, successMessage);
            }
        }
        view.enterToContinue();
    }

    private void editAReservation() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getMessage());

    }

    private void cancelAReservation() throws DataException {
        view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getMessage());

    }

    private Host getHost() throws DataException {
        String lastNamePrefix = view.getHostNamePrefix();
        List<Host> hosts = hostService.findByLastName(lastNamePrefix);
        return view.chooseHost(hosts);
    }

    private Guest selectGuest() throws DataException {
        int selection = view.selectViewGuestsForReservationOption();
        Guest guest = null;
        switch (selection) {
            case 1 :
                guest = view.chooseGuestByEmail(guestService.findAll());
                break;
            case 2 :
                guest = view.chooseGuest(guestService.findByLastName(view.getGuestLastNamePrefix()));
                break;
        }
        if (guest != null) {
            view.displayMessage("Guest selected:");
        }
        view.displayGuest(guest);
        view.enterToContinue();
        return guest;
    }

    private Host selectHost() throws DataException {
        int selection = view.selectViewReservationsForHostOption();
        Host host = null;
        switch (selection) {
            case 1 :
                host = view.chooseHostByEmail(hostService.findAll());
                break;
            case 2 :
                host = view.chooseHost(hostService.findByLastName(view.getHostNamePrefix()));
                break;
        }
        return host;
    }

}
