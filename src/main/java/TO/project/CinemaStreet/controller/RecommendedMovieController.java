package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.*;
import TO.project.CinemaStreet.utils.MailSenderConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

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

    private HallMovieService hallMovieService;
    private HallService hallService;
    private MovieService movieService;

    private CurrentUserContext currentUserContext;
    private UserService userService;
    private EmailService emailService;

    public RecommendedMovieController(HallMovieService hallMovieService,HallService hallService,MovieService movieService) {
        this.hallMovieService = hallMovieService;
        this.hallService = hallService;
        this.movieService=movieService;
        this.currentUserContext = new CurrentUserContext(userService);
        this.emailService = new EmailService(new MailSenderConfig().javaMailSender());
    }

    @FXML
    public void initialize() {
        updateView();
    }
    private void updateView(){
        //reset inputs
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
