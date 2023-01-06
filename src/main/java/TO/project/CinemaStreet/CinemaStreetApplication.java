package TO.project.CinemaStreet;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CinemaStreetApplication {

//	public static void main(String[] args) {
//		SpringApplication.run(CinemaStreetApplication.class, args);
//	}
    public static void main(String[] args) {
        Application.launch(UIApplication.class, args);
    }
}
