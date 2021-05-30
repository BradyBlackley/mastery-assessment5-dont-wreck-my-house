package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.util.List;

public class View {

    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }
        String message = String.format("Select [%s-%s]: ", min, max - 1);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public int selectViewReservationsForHostOption() {
        io.println("1. Find Host by Email");
        io.println("2. Find by Last Name");
        return io.readInt("Select an option [1-2]:", 1, 2);
    }

    public Host chooseHostByEmail(List<Host> hosts) {
        String hostEmail = io.readString("Host Email: ");
        Host host = hosts.stream()
                .filter(h -> h.getEmail().equals(hostEmail))
                .findFirst()
                .orElse(null);
        if (host == null) {
            displayStatus(false, String.format("No host with email %s found.", hostEmail));
        }
        return host;
    }

    public void displayHost(Host host) {
        if (host == null) {
            io.println("Host not found.");
            return;
        }
        io.printf("%s: %s, %s%n",
                host.getLastName(),
                host.getCity(),
                host.getState());
    }



    public void displayReservations(List<Reservation> reservations, Host host) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        displayHost(host);
        for (Reservation reservation : reservations) {
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    reservation.getReservationId(),
                    reservation.getStartDate().toString(),
                    reservation.getEndDate().toString(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getEmail());
        }
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    public void displayErrors(List<String> errors) {
        displayHeader("Errors:");
        for (String error : errors) {
            io.println(error);
        }
    }

    public void displayMessage(String message) {
        io.println("");
        io.println(message);
    }

    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }


}
