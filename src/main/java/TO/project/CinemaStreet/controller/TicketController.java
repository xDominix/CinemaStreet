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
import TO.project.CinemaStreet.utils.FxUtils;
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
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class TicketController {
    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    ComboBox<Hall> hallComboBox;

    @FXML
    ComboBox<Movie> movieComboBox;

    @FXML
    ComboBox<LocalDateTime> dateComboBox;

    @FXML
    TextField seatTextField;

    @FXML
    Label currentSeatsLabel;

    private MovieService movieService;

    private HallMovieService hallMovieService;

    public TicketController(MovieService movieService, HallMovieService hallMovieService) {
        this.movieService = movieService;
        this.hallMovieService = hallMovieService;
    }


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
        seatTextField.setText("1");
//        make sure seatTextField is always integer
        seatTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                seatTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        List<Movie> allMovies = movieService.getAllMovies();
        movieComboBox.setItems(FXCollections.observableArrayList(allMovies));
        FxUtils.autoCompleteComboBoxPlus(movieComboBox, (typedText, movie) -> movie.getName().toLowerCase().contains(typedText.toLowerCase()));
        movieComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Movie movie) {
                if (movie == null) {
                    return null;
                } else {
                    return movie.getName();
                }
            }

            @Override
            public Movie fromString(String movieString) {
                System.out.println("fromString "+movieString);
                System.out.println(allMovies.stream().filter(movie -> movie.getName().equals(movieString)).findFirst().orElse(null));
//                return movie from allMovies
                return allMovies.stream().filter(movie -> movie.getName().equals(movieString)).findFirst().orElse(null);
            }
        });
        ///
//        set a listener to movieComboBox that sets halls in hallComboBox
        movieComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<HallMovie> hallMovies = hallMovieService.getHallMoviesByMovie(newValue);

                hallComboBox.setItems(FXCollections.observableArrayList(hallMovies.stream().map(HallMovie::getHall).collect(Collectors.toList())));
            }
        });
//        set a listener to hallComboBox that sets dates in dateComboBox
        hallComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Movie movie = FxUtils.getComboBoxValue(movieComboBox);
                List<HallMovie> hallMovies = hallMovieService.getHallMoviesByHallAndMovie(newValue, movie);
                List<LocalDateTime> dates = hallMovies.stream().map(HallMovie::getDate).collect(Collectors.toList());
                dateComboBox.setItems(FXCollections.observableArrayList(dates));
            }
        });

        dateComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Hall hall = hallComboBox.getSelectionModel().getSelectedItem();
                Movie movie = FxUtils.getComboBoxValue(movieComboBox);
                System.out.println(hall);
                System.out.println(movie);

                List<HallMovie> hallMovies = hallMovieService.getHallMoviesByHallAndMovie(hall, movie);
                hallMovies = hallMovies.stream().filter(hallMovie -> hallMovie.getDate().equals(newValue)).collect(Collectors.toList());
                currentSeatsLabel.setText(String.valueOf(hallMovies.get(0).howManySeatsLeft()));
            }
        });
    }
    @FXML
    public void buyTicket(ActionEvent actionEvent) {
        Hall hall = hallComboBox.getSelectionModel().getSelectedItem();
        Movie movie = FxUtils.getComboBoxValue(movieComboBox);
        LocalDateTime date = dateComboBox.getSelectionModel().getSelectedItem();
        int seats = Integer.parseInt(seatTextField.getText());
        HallMovie hallMovie = hallMovieService.getHallMovieByHallAndMovieAndDate(hall, movie,date);
//        validate if there are enough seats
        if (hallMovie.howManySeatsLeft() >= seats) {
            hallMovie.setSeatsTaken(hallMovie.getSeatsTaken() + seats);
            hallMovieService.updateHallMovie(hallMovie);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
            alert.setHeaderText("Kupiłeś " + seats + " bilet");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm");
            alert.setContentText("Kupiłeś " + seats + " bilet " + movie.getName() + " w sali " + hall.getId() + " o " +formatter.format(date));
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie ma tylu wolnych miejsc");
            alert.setContentText("Zostało tylko " + hallMovie.howManySeatsLeft() + " miejsc");
            alert.showAndWait();
        }

        currentSeatsLabel.setText(String.valueOf(hallMovie.howManySeatsLeft()));
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
