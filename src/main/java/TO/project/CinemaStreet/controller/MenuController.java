package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.HallService;
import TO.project.CinemaStreet.service.MovieService;
import TO.project.CinemaStreet.service.UserService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
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

    private final HallService hallService;
    private final MovieService movieService;
    private final HallMovieService hallMovieService;

    @FXML
    private FlowPane menuPane;

    private CurrentUserContext currentUserContext;
    private UserService userService;

    public MenuController(HallService hallService, MovieService movieService, HallMovieService hallMovieService, UserService userService) {
        this.hallService = hallService;
        this.movieService = movieService;
        this.hallMovieService = hallMovieService;
        this.userService = userService;
        this.currentUserContext = new CurrentUserContext();
    }

    @FXML
    public void initialize() {
//        createExampleData();
        setupMenuPane();
    }

    @FXML
    public void openStats(ActionEvent actionEvent){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/StatisticsView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 1000, 350);
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
                Scene scene = new Scene(loader.load(), 1200, 900);
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
                Scene scene = new Scene(loader.load(), 1000, 400);
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



            createExampleData();
        } else {
            this.throwError();
        }
    }
    private void createExampleData(){
        addDataButton.setStyle("-fx-background-color: #555555; -fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;");
        addDataButton.setOnMouseExited(e -> addDataButton.setStyle("-fx-background-color: #555555; -fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;"));
        hallService.removeAllHalls();

        List<Hall> newHalls = hallService.getHallsFromJson("src/main/resources/halls.json");
        hallService.addHalls(newHalls);
        System.out.println(hallService.getAllHalls());
        LocalDateTime avatarDate = LocalDateTime.of(2009, 12, 12, 12, 12);
        LocalDateTime interstellarDate = LocalDateTime.of(2014, 11, 7, 0, 0);
        LocalDateTime godfatherDate = LocalDateTime.of(1972, 3, 15, 0, 0);
        movieService.removeAllMovies();
        hallMovieService.removeAllHallMovies();
        Movie movie = new Movie("Avatar", 135, avatarDate, 25.0f, Categories.FANTASY, "https://posters.movieposterdb.com/13_01/2009/499549/l_499549_8fac1d11.jpg");
        Movie movie2 = new Movie("Interstellar", 90, interstellarDate, 10.0f, "https://cdn.shopify.com/s/files/1/0057/3728/3618/products/interstellar5_480x.progressive.jpg?v=1585846879");
        Movie movie3 = new Movie("The Godfather", 175, godfatherDate, 15.0f,Categories.THRILLER, "https://posters.movieposterdb.com/22_12/2006/442674/l_the-godfather-movie-poster_51f17e6f.jpg");
        Movie movie4 = new Movie("Szczęki", 175, godfatherDate.plusMonths(80), 15.0f,Categories.HORROR, "https://posters.movieposterdb.com/08_06/1975/73195/l_73195_a4183f3d.jpg");
        Movie movie5 = new Movie("Smile", 175, godfatherDate.plusMonths(80), 15.0f,Categories.HORROR, "https://posters.movieposterdb.com/22_10/2022/15474916/l_smile-movie-poster_f010c336.jpg");

        movieService.addMovie(movie);
        movieService.addMovie(movie2);
        movieService.addMovie(movie3);
        movieService.addMovie(movie4);
        movieService.addMovie(movie5);

        HallMovie godfatherHallMovie = new HallMovie(hallService.getHallById(1), movie2, LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        hallMovieService.addHallMovie(godfatherHallMovie);

        HallMovie godfatherHallMovie2 = new HallMovie(hallService.getHallById(2), movie2, LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        hallMovieService.addHallMovie(godfatherHallMovie2);

        HallMovie avatarHallMovie = new HallMovie(hallService.getHallById(1), movie, LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS));
        hallMovieService.addHallMovie(avatarHallMovie);

        HallMovie avatarHallMovie2 = new HallMovie(hallService.getHallById(2), movie, LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS));
        hallMovieService.addHallMovie(avatarHallMovie2);

        HallMovie avatarHallMovie3 = new HallMovie(hallService.getHallById(2), movie, LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS));
        hallMovieService.addHallMovie(avatarHallMovie3);

        HallMovie godfatherHallMovie3 = new HallMovie(hallService.getHallById(2), movie3, LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        hallMovieService.addHallMovie(godfatherHallMovie3);

        HallMovie godfatherHallMovie4 = new HallMovie(hallService.getHallById(1), movie3, LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        hallMovieService.addHallMovie(godfatherHallMovie4);

    }

    @FXML
    public void sellTickets(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.SELL_TICKETS)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/TicketView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 367, 215);
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
            Stage currentStage = (Stage) menuPane.getScene().getWindow();
            // do what you have to do
            currentStage.hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
private Button addDataButton;
    private void setupMenuPane() {
//        clear menu pane
        menuPane.getChildren().clear();
        setupMenuButton("Użytkownicy","https://cdn-icons-png.flaticon.com/512/60/60692.png", this::openUsers);
        setupMenuButton("Sprzedaj bilety","https://cdn-icons-png.flaticon.com/512/61/61113.png", this::sellTickets);
        setupMenuButton("Filmy","https://cdn-icons-png.flaticon.com/512/61/61139.png", this::openMovies);
        setupMenuButton("Seanse","https://cdn-icons-png.flaticon.com/512/61/61128.png", this::openHallMovies);
        setupMenuButton("Odśwież sale","https://cdn-icons-png.flaticon.com/512/61/61076.png", this::updateHalls);
        setupMenuButton("Rekomendacje","https://cdn-icons-png.flaticon.com/512/61/61012.png", this::openRecommendedMovies);
        setupMenuButton("Statystyki","https://cdn-icons-png.flaticon.com/512/61/61054.png", this::openStats);
        setupMenuButton("Zalogowany","https://cdn-icons-png.flaticon.com/512/61/61135.png", null);
        addDataButton = setupMenuButton("Stwórz dane","https://cdn-icons-png.flaticon.com/512/60/60745.png", this::createExampleDataAction);
        setupMenuButton("Wyloguj","https://cdn-icons-png.flaticon.com/512/60/60821.png", this::signOut);

//        check if movies are empty
        if (movieService.getAllMovies().isEmpty()) {
//            change its color to green
            addDataButton.setStyle("-fx-background-color: #00ff00; -fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;");
//            on hover exit
            addDataButton.setOnMouseExited(event -> addDataButton.setStyle("-fx-background-color: #00ff00; -fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;"));
        }
    }

    private void createExampleDataAction(ActionEvent actionEvent) {
        createExampleData();
    }

    private Button setupMenuButton(String text, String iconPath, EventHandler<ActionEvent> actionEventEventHandler) {
        Button button = new Button(text);
        double buttonHeight = 140;
        button.setPrefSize(Region.USE_COMPUTED_SIZE, buttonHeight);
        //Create imageview with background image
        ImageView view = new ImageView(iconPath);
//        set its text to black and make it bigger and bold
        String basicStyle = "-fx-background-color: #555555; -fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;";
        button.setStyle(basicStyle);
//        set on hover
        if(actionEventEventHandler != null) {
            button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #AFB1B3; -fx-text-fill: #1A1A1A; -fx-font-size: 20px; -fx-font-weight: bold;"));
        }
        button.setOnMouseExited(e -> button.setStyle(basicStyle));

        view.setFitHeight(buttonHeight);
        view.setPreserveRatio(true);

        button.setGraphic(view);
        button.setContentDisplay(ContentDisplay.TOP);

//        set button on click action
        button.setOnAction(actionEventEventHandler);
        if(actionEventEventHandler == null) {
//            add tooltip
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Username: " + currentUserContext.getUsername()
                    + "\nImię: " + currentUserContext.getFirstname()
                    + "\nNazwisko: " + currentUserContext.getLastname()
                    + "\nRola: " + currentUserContext.getRole());
            button.setTooltip(tooltip);
        }

        menuPane.getChildren().add(button);
        return button;
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
