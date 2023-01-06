package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

//@Component
//@Scope("prototype")
@Controller
public class MenuController {

    @Autowired
    private ConfigurableApplicationContext springContext;

    private HallService hallService;
    private MovieService movieService;
    private HallMovieService hallMovieService;

    @FXML
    private Button signOutButton;
    @FXML
    private Label userInfo;

    private CurrentUserContext currentUserContext;
    private UserService userService;

    public MenuController(HallService hallService, MovieService movieService, HallMovieService hallMovieService, UserService userService) {
        this.hallService = hallService;
        this.movieService = movieService;
        this.hallMovieService = hallMovieService;
        this.userService = userService;
        this.currentUserContext = new CurrentUserContext(userService);
    }

    @FXML
    public void initialize() {
        // TODO additional FX controls initialization
        userInfo.setText("Username: " + currentUserContext.getUsername()
                + "\nImię: " + currentUserContext.getFirstname()
                + "\nNazwisko: " + currentUserContext.getLastname()
                + "\nRola: " + currentUserContext.getRole());
    }

    @FXML
    public void openStats(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/StatisticsView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Statystyki");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void openUsers(ActionEvent actionEvent) {
        // open users view
        try {
            if (currentUserContext.getRole().authorize(Permissions.VIEW_USERS)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/UserView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("Konfiguracja użytkowników");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void openEditMovies(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.VIEW_EDIT_MOVIES)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MovieEditView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("Zarzadzanie filmami");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openMovies(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.VIEW_MOVIES)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MovieView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 1200, 800);
                Stage stage = new Stage();
                stage.setTitle("Filmy");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openHallMovies(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.VIEW_HALLMOVIES)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/HallMovieView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("Zarzadzanie seansami");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateHalls(ActionEvent actionEvent) {
        if (currentUserContext.getRole().authorize(Permissions.UPDATE_HALLS)) {
            // update halls from json

            hallService.removeAllHalls();

            List<Hall> newHalls = hallService.getHallsFromJson("src/main/resources/halls.json");
            hallService.addHalls(newHalls);
            System.out.println(hallService.getAllHalls());
//        hallMovieService.validateHallMovies();
//        hallMovieService.updateAllHallMovies();


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("✔   Sukces");
            alert.setHeaderText(null);
            alert.setContentText("Pomyslnie Zaktualizowano sale");
            alert.showAndWait();

            LocalDateTime avatarDate = LocalDateTime.of(2009, 12, 10, 0, 0);
            LocalDateTime godfatherDate = LocalDateTime.of(1972, 3, 15, 0, 0);

            movieService.removeAllMovies();
            hallMovieService.removeAllHallMovies();
            Movie movie = new Movie("Avatar", 135, avatarDate, 25.0f, Categories.FANTASY, "https://posters.movieposterdb.com/13_01/2009/499549/l_499549_8fac1d11.jpg");
//        Movie movie2 = new Movie("Interstellar", 90, godfatherDate, 10.0f);
            Movie movie2 = new Movie("Interstellar", 90, godfatherDate, 10.0f, "https://cdn.shopify.com/s/files/1/0057/3728/3618/products/interstellar5_480x.progressive.jpg?v=1585846879");

            movieService.addMovie(movie);
            movieService.addMovie(movie2);

            HallMovie godfatherHallMovie = new HallMovie(hallService.getHallById(1), movie2, LocalDateTime.now().plus(2, ChronoUnit.DAYS));
            hallMovieService.addHallMovie(godfatherHallMovie);

            HallMovie godfatherHallMovie2 = new HallMovie(hallService.getHallById(2), movie2, LocalDateTime.now().plus(1, ChronoUnit.DAYS));
            hallMovieService.addHallMovie(godfatherHallMovie2);

            HallMovie avatarHallMovie = new HallMovie(hallService.getHallById(1), movie, LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS));
            hallMovieService.addHallMovie(avatarHallMovie);
        } else {
            this.throwError();
        }
    }

    @FXML
    public void sellTickets(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.SELL_TICKETS)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/TicketView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle("Sprzedaż biletów");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openRecommendedMovies(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.VIEW_RECOMMENDED_MOVIES)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/RecommendedMovieView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 600, 600);
                Stage stage = new Stage();
                stage.setTitle("Rekomendowane filmy");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void signOut(ActionEvent actionEvent) {
        // TODO need to repair closing all windows
        // open users view
        ObservableList<Window> windows = Stage.getWindows();
        for (Window window : windows) {
            if (!window.isFocused() && window.isShowing()) {
                window.hide();
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Logowanie");
            stage.setScene(scene);
            stage.show();

            // get a handle to the stage
            Stage currentStage = (Stage) signOutButton.getScene().getWindow();
            // do what you have to do
            currentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void throwError() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/NoPermissionView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 300, 100);
            Stage stage = new Stage();
            stage.setTitle("Błąd!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
