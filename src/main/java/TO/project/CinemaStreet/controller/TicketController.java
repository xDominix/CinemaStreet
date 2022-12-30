package TO.project.CinemaStreet.controller;
import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.UIApplication;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.Movie;
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Controller
public class TicketController {
    @FXML
    ComboBox<Hall> hallComboBox;

    @FXML
    ComboBox<Movie> movieComboBox;

    @FXML
    ComboBox<LocalDateTime> dateComboBox;

    @FXML
    TextField seatTextField;




    @FXML
    public void initialize() {
        Callback<ListView<LocalDateTime>, ListCell<LocalDateTime>> cellFactory = new Callback<ListView<LocalDateTime>, ListCell<LocalDateTime>>() {

            @Override
            public ListCell<LocalDateTime> call(ListView<LocalDateTime> l) {
                return new ListCell<LocalDateTime>() {

                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm");
                            setText(formatter.format(item));
                        }
                    }
                } ;
            }
        };
        dateComboBox.setButtonCell(cellFactory.call(null));
        dateComboBox.setCellFactory(cellFactory);
        //        put 5 example dates in combobox
        dateComboBox.setItems(FXCollections.observableArrayList(LocalDateTime.now(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2).plusHours(5), LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)));
        seatTextField.setText("1");
//        make sure seatTextField is always integer
        seatTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                seatTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

//        TEMPORARY

//        movieComboBox.setItems(FXCollections.observableArrayList(new Movie(1, "Movie 1", 120), new Movie(2, "Movie 2", 120)));
    }
    @FXML
    public void buyTicket(ActionEvent actionEvent) {
        // TODO implement
    }
}
