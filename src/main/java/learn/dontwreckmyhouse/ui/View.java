package learn.dontwreckmyhouse.ui;

import learn.dontwreckmyhouse.models.Guest;
import learn.dontwreckmyhouse.models.Host;
import learn.dontwreckmyhouse.models.Reservation;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public int selectViewReservationsForHostOption() {
        io.println("1. Find Host by Email");
        io.println("2. Find Host by Last Name");
        return io.readInt("Select an option [1-2]:", 1, 2);
    }

    public int selectViewGuestsForReservationOption() {
        io.println("1. Find Guest by Email");
        io.println("2. Find Guest by Last Name");
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

    public Host chooseHost(List<Host> hosts) {
        if (hosts.size() == 0) {
            io.println("No hosts found");
            return null;
        }

        int index = 1;
        for (Host host : hosts.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s, %s, %s%n", index++, host.getLastName(), host.getCity(), host.getState());
        }
        index--;

        if (hosts.size() > 25) {
            io.println("More than 25 hosts found. Showing first 25. Please refine your search");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
    }

    public String getHostNamePrefix() {
        return io.readRequiredString("Host last name starts with: ");
    }

    public Guest chooseGuestByEmail(List<Guest> guests) {
        String guestEmail = io.readString("Guest Email: ");
        Guest guest = guests.stream()
                .filter(h -> h.getEmail().equals(guestEmail))
                .findFirst()
                .orElse(null);
        if (guest == null) {
            displayStatus(false, String.format("No guest with email %s found.", guestEmail));
        }
        return guest;
    }

    public Guest chooseGuest(List<Guest> guests) {
        if (guests.size() == 0) {
            io.println("No guests found.");
            return null;
        }

        int index = 1;
        for (Guest guest : guests.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: %s, %s, %s, %s%n", index++, guest.getFirstName(),
                    guest.getLastName(), guest.getEmail(), guest.getPhone());
        }
        index--;

        if (guests.size() > 25) {
            io.println("More than 25 guests found. Showing first 25. Please refine your search");
        }
        io.println("0: Exit");
        String message = String.format("Select a guest by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return guests.get(index - 1);
    }

    public Reservation chooseReservation(List<Reservation> reservations) {
        if (reservations.size() == 0) {
            io.println("No reservations found.");
        }

        int index = 1;
        for (Reservation reservation : reservations.stream().collect(Collectors.toList())) {
            io.printf("%s: ID: %s, %s - %s, Guest: %s, %s Email: %s%n", index++,
                    reservation.getReservationId(),
                    reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getEmail());
        }
        index--;

        io.println("0: Exit");
        String message = String.format("Select a reservation by its index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return reservations.get(index - 1);
    }

    public boolean isOkay(){
        return io.readBoolean("Is this okay? [y/n]: ");
    }

    public String getGuestLastNamePrefix() {
        return io.readRequiredString("Guest last name starts with: ");
    }

    public Reservation makeReservation(Host host, Guest guest) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start [MM/DD/YYYY]:"));
        reservation.setEndDate(io.readLocalDate("End [MM/DD/YYYY]:"));
        reservation.updateTotal();
        return reservation;
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

    public void displayGuest(Guest guest) {
        if (guest == null) {
            io.println("Guest not found.");
            return;
        }
        io.printf("%s %s, %s, %s, %s%n",
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getState());
    }

    public void displayReservations(List<Reservation> reservations, Host host) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        displayHost(host);
        reservations.sort(Comparator.comparing(Reservation::getStartDate));
        for (Reservation reservation : reservations) {
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s, Total: $%s%n",
                    reservation.getReservationId(),
                    reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getEmail(),
                    reservation.getTotal());
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

    public void displayReservationSummary(Reservation reservation) {
        io.printf("Start: %s%n",
                reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        io.printf("End: %s%n",
                reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
        io.printf("Total: $%s%n",
                reservation.getTotal());
    }

    public void editReservation(Reservation reservation) {
        displayHeader(String.format("Editing Reservation %s%n", reservation.getReservationId()));
        reservation.setStartDate(io.readLocalDate(String.format("Start (%s): ",
                reservation.getStartDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))));
        reservation.setEndDate(io.readLocalDate(String.format("End (%s): ",
                reservation.getEndDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)))));
    }

    public void CancelReservation(Reservation reservation) {
        String.format("Reservation ID: %s", reservation.getReservationId());
    }


}
