package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.*;
import TO.project.CinemaStreet.utils.FxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class HallMovieController
{
    @Autowired
    private ConfigurableApplicationContext springContext;
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
    private TextField hourPicker;
    @FXML
    private TextField minutePicker;
    @FXML
    private TextField searchTextField;
    @FXML
    private ComboBox<Categories> searchByCategoryComboBox;


    private HallMovieService hallMovieService;
    private HallService hallService;
    private MovieService movieService;
    private FilterHallMovieService filterHallMovieService;
    private FilteredList<HallMovie> filteredList;

    public HallMovieController(HallMovieService hallMovieService,HallService hallService,MovieService movieService,FilterHallMovieService filterHallMovieService)
    {
        this.hallMovieService = hallMovieService;
        this.hallService = hallService;
        this.movieService = movieService;
        this.filterHallMovieService = filterHallMovieService;
    }

    @FXML
    public void initialize() {
        //initialize filteredList with all movies, so that it can be filtered later, set up handlers in searchBars
        filteredList = filterHallMovieService.getFilteredList();
        searchTextField.textProperty().addListener(filterHallMovieService.getNameInputListener());
        searchByCategoryComboBox.getItems().addAll(Categories.values());
        FxUtils.autoCompleteComboBoxPlus(searchByCategoryComboBox, (typedText, itemToCompare) -> itemToCompare.name().toLowerCase().contains(typedText.toLowerCase()));
        searchByCategoryComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Categories object) {
                if(object == null) return "";
                return object.name();
            }
            @Override
            public Categories fromString(String string) {
                if(string == null) return null;
//                check if string is in enum
                for(Categories category : Categories.values()){
                    if(category.name().equals(string)){
                        return category;
                    }
                }
                return null;
            }
        });
        searchByCategoryComboBox.valueProperty().addListener(filterHallMovieService.getCategoryInputListener());




        hallMovieListView.setItems(filteredList);
        filteredList.addListener((ListChangeListener<HallMovie>) c -> {
                   hallMovieListView.setItems(filteredList);
        });
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
                        setText(item.getId()+", "+item.getMovie().getName()+", "+item.getHall().getId()+", "+item.getDate().toLocalDate()+
                                " "+item.getDate().toLocalTime().toString().substring(0,5));
                    else
                        setText(null);
                }
            }
        });

//        searchTextField.textProperty().addListener((observable, oldValue, newValue)->{
//            hallMovieListView.setItems(FXCollections.observableArrayList
//                    (hallMovieService.getAllHallMovies().stream()
//                            .filter(el->el.getMovie().getName().toLowerCase().contains(newValue.toLowerCase())).toList()));
//        });

        updateView();
    }
    private void updateView(){
        //reset inputs
        idTextField.setText("");

        hourPicker.setText(null);
        minutePicker.setText(null);
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
    }//?
    @FXML
    private void addHallMovie(ActionEvent actionEvent) {
        if(checkInputs())
        {
                Hall hall = hallComboBox.getValue();
                Movie movie = movieComboBox.getValue();

                LocalDate localDate = datePicker.getValue();
                int hour = Integer.parseInt(hourPicker.getText());
                int minute = Integer.parseInt(minutePicker.getText());

                LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(),localDate.getMonth(),localDate.getDayOfMonth(),hour,minute);

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
        }else if(hourPicker.getText()==null || !isNumeric(hourPicker.getText()))
        {
            hourPicker.getStyleClass().add("error");
            return false;
        }  else if(minutePicker.getText()==null ||  !isNumeric(minutePicker.getText()))
        {
            minutePicker.getStyleClass().add("error");
            return false;
        }
       return true;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
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
