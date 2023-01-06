package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.HallService;
import TO.project.CinemaStreet.service.MovieService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class HallMovieController
{
    @FXML
    ListView<HallMovie> hallMovieListView = new ListView<HallMovie>();

    @FXML
    private TextField idTextField;

    @FXML
    private ComboBox<Hall> hallComboBox;

    @FXML
    private ComboBox<Movie> movieComboBox;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField searchTextField;


    private HallMovieService hallMovieService;
    private HallService hallService;
    private MovieService movieService;

    public HallMovieController(HallMovieService hallMovieService,HallService hallService,MovieService movieService) {
        this.hallMovieService = hallMovieService;
        this.hallService = hallService;
        this.movieService=movieService;
    }

    @FXML
    public void initialize() {

        hallMovieListView.setItems(FXCollections.observableArrayList(hallMovieService.getAllHallMovies()));
        hallComboBox.setItems(FXCollections.observableArrayList(hallService.getAllHalls()));
        movieComboBox.setItems(FXCollections.observableArrayList(movieService.getAllMovies()));


        hallMovieListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(HallMovie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null ) {
                    setText(null);
                } else {
                    if(item.getMovie().getName().contains(""))
                        setText(item.getId()+", "+item.getMovie().getName()+", "+item.getHall().getId()+", "+item.getDate());
                    else
                        setText(null);
                }
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue)->{
            hallMovieListView.setItems(FXCollections.observableArrayList
                    (hallMovieService.getAllHallMovies().stream()
                            .filter(el->el.getMovie().getName().toLowerCase().contains(newValue.toLowerCase())).toList()));
        });

        updateView();
    }
    private void updateView(){
        //reset inputs
        idTextField.setText("");

        datePicker.setValue(null);
        movieComboBox.valueProperty().set(null);
        hallComboBox.valueProperty().set(null);

        //update list
        ObservableList<HallMovie> items =   FXCollections.observableArrayList(hallMovieService.getAllHallMovies());
        hallMovieListView.setItems(items);
    }

    public HallMovie getHallMovie(int id)
    {
        return null;
    }
    @FXML
    private void addHallMovie(ActionEvent actionEvent) {
        if(checkInputs())
        {


                Hall hall = hallComboBox.getValue();
                Movie movie = movieComboBox.getValue();

                LocalDate localDate = datePicker.getValue();
                LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth(),0,0);


                HallMovie hallMovie = new HallMovie(hall,movie,localDateTime);
                hallMovieService.addHallMovie(hallMovie);
                updateView();


        }
    }

    @FXML
    private void deleteHallMovie(ActionEvent actionEvent) {
        removeInputsErrors();
        try{
            boolean deleted = hallMovieService.deleteHallMovieById(Integer.parseInt(idTextField.getText()));

            if(!deleted){
                idTextField.getStyleClass().add("error");
                System.out.println("HallMovie does not exists");
                return;
            }

            System.out.println("Deleted");
            updateView();
        }
        catch (NumberFormatException ex){
            idTextField.getStyleClass().add("error");
            System.out.println("Input is NaN");
        }

    }

    private boolean checkInputs(){
        removeInputsErrors();
        if(hallComboBox.getValue()==null) {
            hallComboBox.getStyleClass().add("error");
            return false;
        }
        else if(movieComboBox.getValue()==null) {
            movieComboBox.getStyleClass().add("error");
            return false;
        }
        else if(datePicker.getValue()==null || datePicker.getValue().isBefore(LocalDate.now()))
        {
            datePicker.getStyleClass().add("error");
            return false;
        }
       return true;
    }

    private void removeInputsErrors(){
        idTextField.getStyleClass().remove("error");
        datePicker.getStyleClass().remove("error");
        hallComboBox.getStyleClass().remove("error");
        movieComboBox.getStyleClass().remove("error");

    }

}
