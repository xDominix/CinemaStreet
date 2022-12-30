package TO.project.CinemaStreet.controller;
import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.UIApplication;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.service.HallService;
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
import java.util.List;
import java.util.Objects;

//@Component
//@Scope("prototype")
@Controller
public class MenuController {

    @Autowired
    private ConfigurableApplicationContext springContext;

    private HallService hallService;

    public MenuController(HallService hallService) {
        this.hallService = hallService;
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
    }
}
