package TO.project.CinemaStreet.controller;
import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.UIApplication;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.HallService;
import TO.project.CinemaStreet.service.MovieService;
import TO.project.CinemaStreet.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

//@Component
//@Scope("prototype")
@Controller
public class MenuController {

    @Autowired
    private ConfigurableApplicationContext springContext;

    private HallService hallService;
    private MovieService movieService;
    private HallMovieService hallMovieService;

    public MenuController(HallService hallService, MovieService movieService, HallMovieService hallMovieService) {
        this.hallService = hallService;
        this.movieService = movieService;
        this.hallMovieService = hallMovieService;
    }

    @FXML
    public void initialize() {
        // TODO additional FX controls initialization
    }

    @FXML
    public void openUsers(ActionEvent actionEvent) {
        // open users view
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/UserView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Konfiguracja użytkowników");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void openHallMovies(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/HallMovieView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Zarzadzanie seansami");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void updateHalls(ActionEvent actionEvent) {
        // update halls from json

//        alert.showAndWait();
        hallService.removeAllHalls();

        List<Hall> newHalls = hallService.getHallsFromJson("src/main/resources/halls.json");
        hallService.addHalls(newHalls);
//        System.out.println(hallService.getAllHalls());
//        TODO When hallmovieController is created: validate if all screenings are valid



        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✔   Sukces");
        alert.setHeaderText(null);
        alert.setContentText("Pomyslnie Zaktualizowano sale");
        alert.showAndWait();

        LocalDateTime avatarDate = LocalDateTime.of(2009, 12, 10, 0, 0);
        LocalDateTime godfatherDate = LocalDateTime.of(1972, 3, 15, 0, 0);

        movieService.removeAllMovies();
        hallMovieService.removeAllHallMovies();
        Movie movie = new Movie("Avatar", 135, avatarDate, 25.0f);
        Movie movie2 = new Movie("Godfather", 90, godfatherDate, 10.0f);

        movieService.addMovie(movie);
        movieService.addMovie(movie2);

        HallMovie godfatherHallMovie = new HallMovie(hallService.getHallById(1),movie2, LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        hallMovieService.addHallMovie(godfatherHallMovie);

        HallMovie godfatherHallMovie2 = new HallMovie(hallService.getHallById(2),movie2, LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        hallMovieService.addHallMovie(godfatherHallMovie2);

        HallMovie avatarHallMovie = new HallMovie(hallService.getHallById(1),movie, LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS));
        hallMovieService.addHallMovie(avatarHallMovie);


    }
    @FXML
    public void sellTickets(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/TicketView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Sprzedaż biletów");
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
