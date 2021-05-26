package learn.dontwreckmyhouse.models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
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

    public void updateTotal() {
        this.setTotal(this.calculateTotal());
    }

    private BigDecimal calculateTotal() {
        LocalDate temp = startDate;
        boolean done = false;
        BigDecimal weekDays = BigDecimal.valueOf(0);
        BigDecimal weekEndDays = BigDecimal.valueOf(0);

        while (!done) {
            if (temp.isEqual(endDate)) {
                done = true;
            } else if (temp.getDayOfWeek() == DayOfWeek.SATURDAY || temp.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekEndDays = weekEndDays.add(BigDecimal.valueOf(1));
            } else {
                weekDays = weekDays.add(BigDecimal.valueOf(1));
            }
            temp = temp.plusDays(1);
        }

        BigDecimal weekDayTotal = new BigDecimal(String.valueOf(weekDays.multiply(host.getStandardRate())));
        BigDecimal weekEndTotal = new BigDecimal(String.valueOf(weekEndDays.multiply(host.getWeekendRate())));

        return weekDayTotal.add(weekEndTotal);
    }

    //TODO: include calculate total helper method

    //TODO: include boolean to check if there is overlap in start date
}
