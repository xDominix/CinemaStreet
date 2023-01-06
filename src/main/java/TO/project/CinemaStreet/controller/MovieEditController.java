package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.service.HallService;
import TO.project.CinemaStreet.service.MovieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;

@Controller
public class MovieEditController
{
    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    ListView<Movie> movieListView = new ListView<Movie>();

    @FXML
    private TextField idTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField lengthTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField ticketCostTextField;

    private MovieService movieService;

    public MovieEditController(MovieService movieService) {
        this.movieService = movieService;
    }

    @FXML
    private TextField searchTextField;

    @FXML
    public void initialize() {
        movieListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Movie item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getId()+" '"+item.getName()+"' "+item.getLength()+"min "+item.getTicketCost()+"$ "+item.getReleaseDate().toLocalDate());
                }
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue)->{
            movieListView.setItems(FXCollections.observableArrayList
                    (FXCollections.observableArrayList(movieService.getAllMovies()).stream()
                            .filter(el->el.getName().toLowerCase().contains(newValue.toLowerCase())).toList()));
        });


        updateView();
    }
    private void updateView(){
        //reset inputs
        nameTextField.setText("");
        lengthTextField.setText("");
        ticketCostTextField.setText("");
        datePicker.setValue(null);
        idTextField.setText("");

        //update list
        ObservableList<Movie> items =   FXCollections.observableArrayList(movieService.getAllMovies());
        movieListView.setItems(items);
    }

    @FXML
    private void addNewMovie(ActionEvent actionEvent) {
        if(checkInputs())
        {

            try{
                int length = Integer.parseInt(lengthTextField.getText());
                float ticketCost = Float.parseFloat(ticketCostTextField.getText());

                Movie movie = new Movie(nameTextField.getText(),length,datePicker.getValue().atStartOfDay(),ticketCost);
                movieService.addMovie(movie);
                updateView();
            }
            catch (NumberFormatException ex){
                ex.printStackTrace();
            }

        }
    }

    @FXML
    private void deleteMovie(ActionEvent actionEvent) {
        removeInputsErrors();
        try{
            boolean deleted = movieService.deleteMovieById(Integer.parseInt(idTextField.getText()));

            if(!deleted){
                idTextField.getStyleClass().add("error");
                System.out.println("User does not exists");
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

        if(nameTextField.getText().length()<3) {
            nameTextField.getStyleClass().add("error");
            return false;
        } else if(!isInt(lengthTextField.getText()))
        {
            lengthTextField.getStyleClass().add("error");
            return false;
        }else if(datePicker.getValue()==null || datePicker.getValue().isAfter(LocalDate.now()))
        {
            datePicker.getStyleClass().add("error");
            return false;
        }else if(!isFloat(ticketCostTextField.getText()))
        {
            ticketCostTextField.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    private void removeInputsErrors(){
        idTextField.getStyleClass().remove("error");
        nameTextField.getStyleClass().remove("error");
        lengthTextField.getStyleClass().remove("error");
        datePicker.getStyleClass().remove("error");
        ticketCostTextField.getStyleClass().remove("error");
    }

    public static boolean isInt(String strNum) {
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
    public static boolean isFloat(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Float.parseFloat(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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

