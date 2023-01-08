package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.model.RecommendedMovie;
import TO.project.CinemaStreet.service.*;
import TO.project.CinemaStreet.utils.MailSenderConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class RecommendedMovieController
{
    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    ListView<HallMovie> recommendedMovieListView = new ListView<HallMovie>();

    @FXML
    private TextField idTextField;

    @FXML
    private ComboBox<Hall> hallComboBox;

    @FXML
    private ComboBox<Movie> movieComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private FlowPane movieFlowPane;

    private HallMovieService hallMovieService;
    private HallService hallService;
    private MovieService movieService;

    private CurrentUserContext currentUserContext;
    private UserService userService;
    private EmailService emailService;

    private RecommendedMovieService recommendedMovieService;

    private ObservableList<RecommendedMovie> recommendedMovies;


    public RecommendedMovieController(HallMovieService hallMovieService,HallService hallService,MovieService movieService, RecommendedMovieService recommendedMovieService) {
        this.hallMovieService = hallMovieService;
        this.hallService = hallService;
        this.movieService=movieService;
        this.recommendedMovieService = recommendedMovieService;
        this.currentUserContext = new CurrentUserContext(userService);
        this.emailService = new EmailService(new MailSenderConfig().javaMailSender());
    }

    @FXML
    public void initialize() {
        movieFlowPane.setHgap(20);
        updateView();
    }
    private void updateView(){
        recommendedMovies = FXCollections.observableList(recommendedMovieService.getAllRecommendedMovies());
        setMoviesToPane(recommendedMovies);
    }

    private void setMoviesToPane(List<RecommendedMovie> movies){
        if(movieFlowPane == null){
            System.out.println("movieFlowPane is null");
            return;
        }
        movieFlowPane.getChildren().clear();
        for (RecommendedMovie movie :
                movies) {
            VBox movieCard = createMovieCard(movie.getMovie());
            movieFlowPane.getChildren().add(movieCard);
        }
    }

    @FXML
    public void sendMails(ActionEvent actionEvent) {
        try {
            if (currentUserContext.getRole().authorize(Permissions.SEND_MAILS)) {
                emailService.send("cinemastreet1@gmail.com", "veraqualitas@gmail.com",
                        "Hello", "How are you?");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/SuccessView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 300, 100);
                Stage stage = new Stage();
                stage.setTitle("Sukces!");
                stage.setScene(scene);
                stage.show();
            } else {
                this.throwError();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox createMovieCard(Movie movie){
        Image image = new Image(movie.getImageUrl());
        ImageView imageView = new ImageView(image);
        float multiplier = 0.8f;
        imageView.setFitHeight(450*multiplier);
        imageView.setFitWidth(300*multiplier);


        Label movieTitle = new Label(movie.getName());
        movieTitle.setPadding(new Insets(0,0,0,0));
        movieTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;-fx-cursor: hand;");
        VBox movieCard = new VBox(imageView, movieTitle, setupRemoveButton("Usuń", "https://cdn-icons-png.flaticon.com/128/60/60994.png", movie));
        movieCard.setPadding(new Insets(10,10,10,10));
        movieCard.setSpacing(0);
        movieCard.setAlignment(javafx.geometry.Pos.CENTER);

        movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");


//        set on hover
        movieCard.setOnMouseEntered(event -> {
            movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, #ffffff, 10, 0, 0, 0);");
        });
        movieCard.setOnMouseExited(event -> {
            movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");
        });

        return movieCard;
    }

    private Button setupRemoveButton(String text, String iconPath, Movie movie){
        Button button = new Button(text);
        double buttonSize = 20;
        button.setPrefSize(buttonSize, buttonSize);

        ImageView view = new ImageView(iconPath);
        String basicStyle = "-fx-background-color: #555555; -fx-text-fill: #1A1A1A; -fx-font-size: 8px; -fx-font-weight: bold;";
        button.setStyle(basicStyle);

        button.setOnMouseEntered(e ->
                button.setStyle("-fx-background-color: #AFB1B3; -fx-text-fill: #1A1A1A; -fx-font-size: 8px; -fx-font-weight: bold;"));

        button.setOnMouseExited(e -> button.setStyle(basicStyle));

        view.setFitHeight(buttonSize);
        view.setPreserveRatio(true);

        button.setGraphic(view);
        button.setContentDisplay(ContentDisplay.TOP);

        button.setOnAction(e -> {
            recommendedMovieService.deleteRecommendedMovie(movie);
            updateView();
        });

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
