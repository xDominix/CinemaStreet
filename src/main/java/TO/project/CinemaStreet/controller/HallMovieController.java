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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDate;
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
    private ComboBox<String> hallComboBox;

    @FXML
    private ComboBox<String> movieComboBox;

    @FXML
    private DatePicker datePicker;

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

        List<String> halls = hallService.getAllHalls().stream().map(key -> key.getId() + " (" + key.getSeatsNumber()+")").collect(Collectors.toList())
                .stream().map(Object::toString).collect(Collectors.toList());;
        hallComboBox.getItems().setAll(halls);

        List<String> movies = movieService.getAllMovies().stream().map(key -> key.getName()).collect(Collectors.toList());
        movieComboBox.getItems().setAll(movies);

        hallMovieListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(HallMovie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getHall() == null) {
                    setText(null);
                } else {
                    setText(item.getId()+" "+item.getHall().getId()+" "+item.getMovie().getName()+" "+item.getDate());
                }
            }
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
            System.out.println(hallComboBox.getValue().toString()+" "+movieComboBox.getValue().toString()+" "+datePicker.getValue().toString());

            int hallID = hallComboBox.getSelectionModel().getSelectedIndex();
            Hall hall = hallService.getHallById(hallID);

            int movieID = movieComboBox.getSelectionModel().getSelectedIndex();
            Movie movie = movieService.getMovieById(movieID);

            Date date = Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            HallMovie hallMovie = new HallMovie(hall,movie,date);
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
        else if(datePicker.getValue()==null || datePicker.getValue().isAfter(LocalDate.now()))
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
