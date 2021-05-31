package learn.dontwreckmyhouse;

import learn.dontwreckmyhouse.data.GuestFileRepository;
import learn.dontwreckmyhouse.data.HostFileRepository;
import learn.dontwreckmyhouse.data.ReservationFileRepository;
import learn.dontwreckmyhouse.domain.GuestService;
import learn.dontwreckmyhouse.domain.HostService;
import learn.dontwreckmyhouse.domain.ReservationService;
import learn.dontwreckmyhouse.ui.ConsoleIO;
import learn.dontwreckmyhouse.ui.Controller;
import learn.dontwreckmyhouse.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan
@PropertySource("classpath:data.properties")
public class App {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);

        Controller controller = context.getBean(Controller.class);

//        GuestFileRepository guestRepository
//                = new GuestFileRepository("./data/app-data/guests.csv");
//        HostFileRepository hostRepository
//                = new HostFileRepository("./data/app-data/hosts.csv");
//        ReservationFileRepository reservationRepository
//                = new ReservationFileRepository("./data/app-data/reservations",
//                guestRepository, hostRepository);
//
//        GuestService guestService =
//                new GuestService(guestRepository);
//        HostService hostService =
//                new HostService(hostRepository);
//        ReservationService reservationService =
//                new ReservationService(reservationRepository, guestRepository, hostRepository);
//
//        ConsoleIO io = new ConsoleIO();
//        View view = new View(io);
//        Controller controller = new Controller(guestService, hostService, reservationService, view);
        controller.run();

    }
}
