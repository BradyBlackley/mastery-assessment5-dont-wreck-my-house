package learn.dontwreckmyhouse.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reservation {

    private int reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Guest guest;
    private Host host;
    private BigDecimal total;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    //TODO: include calculate total helper method

    //TODO: include boolean to check if there is overlap in start date
}
