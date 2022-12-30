package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.HallService;
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
    private StringProperty selectedHall= new SimpleStringProperty(this, "hall");;
    @FXML
    private ComboBox<String> movieComboBox;
    private StringProperty selectedMovie= new SimpleStringProperty(this, "movie");

    @FXML
    private DatePicker datePicker;
    private ObjectProperty<Date> selectedDate= new SimpleObjectProperty<Date>(this, "date");

    private HallMovieService hallMovieService;
    private HallService hallService;


    public HallMovieController(HallMovieService hallMovieService,HallService hallService) {
        this.hallMovieService = hallMovieService;
        this.hallService = hallService;
    }

    @FXML
    public void initialize() {

        List<String> hallIds = hallService.getAllHalls().stream().map(key -> key.getId() + " (" + key.getSeatsNumber()+")").collect(Collectors.toList())
                .stream().map(Object::toString).collect(Collectors.toList());;

        hallComboBox.getItems().setAll(hallIds);
        selectedHall.bind(hallComboBox.getSelectionModel().selectedItemProperty());
        movieComboBox.getItems().setAll("Avatar", "Sex w wielkim miescie", "Glass Onion");//TODO
        selectedMovie.bind(movieComboBox.getSelectionModel().selectedItemProperty());

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

        //TODO handler nie dziala
        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                LocalDate date = datePicker.getValue();
                selectedDate.set(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                System.err.println("Selected date: " + date);
            }
        });

        updateView();
    }
    private void updateView(){
        //reset inputs
        //selectedHall.set("");
        //selectedMovie.set("");
        //selectedDate.set(new Date());

        datePicker.setValue(null);
        idTextField.setText("");

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
            System.out.println(selectedHall.toString()+" "+selectedDate.toString()+" "+selectedMovie.toString());
            //TODO
            //HallMovie hallMovie = new HallMovie(selectedHall.getText(), selectedMovie.getText(), dataPicker);
            //hallMovieService.addHallMovie(hallMovie);
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
        //TODO errory nie koloruja sie
        if(selectedDate.get()==null || selectedDate.get().before(new Date()))
        {
            System.out.println("date error");
            datePicker.getStyleClass().add("error");
            return false;
        }
        else if(selectedMovie.get().length()==0) {
            System.out.println("movie error");
            movieComboBox.getStyleClass().add("error");
            return false;
        }
        else if(selectedHall.get().length()==0) {
            System.out.println("hall error");
            hallComboBox.getStyleClass().add("error");
            return false;
        }
       return true;
    }

    private void removeInputsErrors(){
        idTextField.getStyleClass().remove("error");
        datePicker.getStyleClass().remove("error");
    }

}
