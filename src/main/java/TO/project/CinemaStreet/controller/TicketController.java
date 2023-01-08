package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.FilterMovieService;
import TO.project.CinemaStreet.service.HallMovieService;
import TO.project.CinemaStreet.utils.FxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TicketController {
    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    ComboBox<Hall> hallComboBox;
    @FXML
    ComboBox<Categories> categoryComboBox;
    @FXML
    ComboBox<Movie> movieComboBox;
    @FXML
    ComboBox<LocalDateTime> dateComboBox;
    @FXML
    TextField seatTextField;
    @FXML
    Label currentSeatsLabel;

    private final FilterMovieService filterMovieService;
    private final HallMovieService hallMovieService;

    public TicketController(HallMovieService hallMovieService, FilterMovieService filterMovieService) {
        this.hallMovieService = hallMovieService;
        this.filterMovieService = filterMovieService;
    }

    private FilteredList<Movie> filteredList;
    @FXML
    public void initialize() {
        filteredList = filterMovieService.getFilteredList();
        movieComboBox.setItems(filteredList);
//        if movie combo box selection changes reset all other combo boxes
        movieComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> clearAll());
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
            public Movie fromString(String string) {
                return filteredList.stream().filter(movie -> movie.getName().equals(string)).findFirst().orElse(null);
            }
        });
//        movieComboBox.valueProperty().addListener(filterMovieService.getNameInputListener());
        categoryComboBox.getItems().addAll(Categories.values());
        FxUtils.autoCompleteComboBoxPlus(categoryComboBox, (typedText, itemToCompare) -> itemToCompare.name().toLowerCase().contains(typedText.toLowerCase()));
        categoryComboBox.setConverter(new StringConverter<>() {
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
        categoryComboBox.valueProperty().addListener(filterMovieService.getCategoryInputListener());

        Callback<ListView<LocalDateTime>, ListCell<LocalDateTime>> cellFactory = new Callback<>() {

            @Override
            public ListCell<LocalDateTime> call(ListView<LocalDateTime> l) {
                return new ListCell<>() {
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
                };
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
//        set a listener to movieComboBox that sets halls in hallComboBox
        movieComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<HallMovie> hallMovies = hallMovieService.getHallMoviesByMovie(newValue);

//                dont set duplicate halls
                hallComboBox.setItems(FXCollections.observableArrayList(hallMovies.stream().map(HallMovie::getHall).collect(Collectors.toSet())));
            }
        });
//        set a listener to hallComboBox that sets dates in dateComboBox
        hallComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Movie movie = FxUtils.getComboBoxValue(movieComboBox);
                if(movie == null){
                    throw new RuntimeException("Movie is null");
                }
                List<HallMovie> hallMovies = hallMovieService.getHallMoviesByHallAndMovie(newValue, movie);
                List<LocalDateTime> dates = hallMovies.stream().map(HallMovie::getDate).collect(Collectors.toList());
                dateComboBox.setItems(FXCollections.observableArrayList(dates));
            }
        });

        dateComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Hall hall = hallComboBox.getSelectionModel().getSelectedItem();
                Movie movie = FxUtils.getComboBoxValue(movieComboBox);
                if(hall == null || movie == null){
                    throw new RuntimeException("Hall or movie is null");
                }

                List<HallMovie> hallMovies = hallMovieService.getHallMoviesByHallAndMovie(hall, movie);
                hallMovies = hallMovies.stream().filter(hallMovie -> hallMovie.getDate().equals(newValue)).collect(Collectors.toList());
                currentSeatsLabel.setText(String.valueOf(hallMovies.get(0).howManySeatsLeft()));
            }
        });
        filteredList.addListener((ListChangeListener<Movie>) c -> {
            movieComboBox.setItems(filteredList);
        });
    }
    private void clearAll() {
        hallComboBox.getSelectionModel().clearSelection();
        dateComboBox.getSelectionModel().clearSelection();
        seatTextField.setText("1");
        currentSeatsLabel.setText("0");
    }
    @FXML
    public void buyTicket(ActionEvent actionEvent) {
        Hall hall = hallComboBox.getSelectionModel().getSelectedItem();
        Movie movie = FxUtils.getComboBoxValue(movieComboBox);
        LocalDateTime date = dateComboBox.getSelectionModel().getSelectedItem();
        int seats = Integer.parseInt(seatTextField.getText());

//        validate if all fields are filled
        if (hall == null || movie == null || date == null || seats == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie wypełniłeś wszystkich pól");
            alert.setContentText("Wypełnij wszystkie pola");
            alert.showAndWait();
            return;
        }

        HallMovie hallMovie = hallMovieService.getHallMovieByHallAndMovieAndDate(hall, movie,date);

//        validate if there are enough seats
        if (hallMovie.howManySeatsLeft() >= seats) {
            hallMovie.setSeatsTaken(hallMovie.getSeatsTaken() + seats);
            hallMovieService.updateHallMovie(hallMovie);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sukces");
//            if more then 1 then "biletów" else "bilet"
            alert.setHeaderText("Kupiłeś " + seats + (seats > 1 ? " biletów" : " bilet"));
            alert.setContentText("Kupiłeś " + seats + (seats > 1 ? " biletów" : " bilet") + movie.getName() + " w sali " + hall.getId() + " o " +formatter.format(date));
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
