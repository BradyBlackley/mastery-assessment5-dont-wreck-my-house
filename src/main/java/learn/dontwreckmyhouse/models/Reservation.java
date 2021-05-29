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

    public Reservation() {

    }

    public Reservation(Guest guest, Host host) {
        this.guest = guest;
        this.host = host;
    }

    public Reservation(int reservationId, LocalDate startDate, LocalDate endDate, Guest guest, Host host) {
        this.reservationId = reservationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guest = guest;
        this.host = host;
        updateTotal();
    }

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
        BigDecimal weekDayTotal;
        BigDecimal weekEndTotal;

        if(startDate.isAfter(endDate)){
            weekDayTotal = BigDecimal.ZERO;
            weekEndTotal = BigDecimal.ZERO;
        }else {
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

            weekDayTotal = new BigDecimal(String.valueOf(weekDays.multiply(host.getStandardRate())));
            weekEndTotal = new BigDecimal(String.valueOf(weekEndDays.multiply(host.getWeekendRate())));
        }
        return weekDayTotal.add(weekEndTotal);
    }
}
