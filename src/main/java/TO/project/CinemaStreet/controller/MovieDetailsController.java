package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.model.RecommendedMovie;
import TO.project.CinemaStreet.service.AuthorizationService;
import TO.project.CinemaStreet.service.FilterMovieService;
import TO.project.CinemaStreet.service.MovieService;
import TO.project.CinemaStreet.service.RecommendedMovieService;
import TO.project.CinemaStreet.utils.FxUtils;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;


@Controller
public class MovieDetailsController {
    @Autowired
    private ConfigurableApplicationContext springContext;
    private Movie currentMovie;

    @FXML
    private ImageView imageView;
    @FXML
    VBox detailsVBox;
    @FXML
    private Label titleLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private Label releaseDateLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label ticketCostLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Button deleteButton;

    @FXML
    private Button recommendationButton;

    private MovieService movieService;
    private RecommendedMovieService recommendedMovieService;
    private FilterMovieService filterMovieService;
    private AuthorizationService authorizationService;

    public MovieDetailsController(MovieService movieService, RecommendedMovieService recommendedMovieService, FilterMovieService filterMovieService, AuthorizationService authorizationService) {
        this.movieService = movieService;
        this.recommendedMovieService = recommendedMovieService;
        this.filterMovieService = filterMovieService;
        this.authorizationService = authorizationService;
    }

    @FXML
    public void initialize() {
//        set color of text to red of deleteButton
        deleteButton.setStyle("-fx-text-fill: red");
//        placeholder image in case of no image
        imageView.setImage(new javafx.scene.image.Image("https://posters.movieposterdb.com/20_01/2017/7131440/l_7131440_be0c6b24.jpg"));
    }
    public void setMovie(Movie movie) {
        currentMovie = movie;
        if (recommendedMovieService.getRecommendedMovieByMovie(movie) != null) {
            recommendationButton.setText("Nie rekomenduj");
        }
        updateView();
    }
    private void updateView() {
        imageView.setImage(new javafx.scene.image.Image(currentMovie.getImageUrl()));
        titleLabel.setText(currentMovie.getName());
        idLabel.setText(String.valueOf(currentMovie.getId()));
        categoryLabel.setText(currentMovie.getCategory());
//        format date to dd-MM-yyyy
        releaseDateLabel.setText(currentMovie.getReleaseDate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy")));
//        format to h min
        durationLabel.setText(String.format("%d h %d min", currentMovie.getLength() / 60, currentMovie.getLength() % 60));
        ticketCostLabel.setText(currentMovie.getTicketCost() + "$");
        GridPane.setHalignment(categoryLabel, HPos.RIGHT);
        GridPane.setHalignment(durationLabel, HPos.RIGHT);
        GridPane.setHalignment(idLabel, HPos.RIGHT);
//        if category empty then set NIE USTAWIONO
        if(currentMovie.getCategory() == ""){
            categoryLabel.setText("NIE USTAWIONO");
        }
//        detailsVBox set background color
        detailsVBox.setStyle("-fx-background-color: #4e4e4e;fx-background-radius: 10px;-fx-padding: 10px;-fx-border-radius: 5px; -fx-border-color: #AFB1B3; -fx-border-width: 2px");
//        White titleLabel
        titleLabel.setStyle("-fx-text-fill: white");

//        set tooltips for all labels
        titleLabel.setTooltip(new javafx.scene.control.Tooltip("Tytuł"));
        categoryLabel.setTooltip(new javafx.scene.control.Tooltip("Gatunek"));
        releaseDateLabel.setTooltip(new javafx.scene.control.Tooltip("Data premiery"));
        durationLabel.setTooltip(new javafx.scene.control.Tooltip("Czas trwania"));
        ticketCostLabel.setTooltip(new javafx.scene.control.Tooltip("Cena biletu"));
        idLabel.setTooltip(new javafx.scene.control.Tooltip("ID"));

    }
    public void copyUrlAction(ActionEvent actionEvent){
//        copy url of the movie to the system clipboard
        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(currentMovie.getImageUrl());
        clipboard.setContent(content);
    }

    public void addToRecommendedAction(ActionEvent actionEvent) throws SQLException {
        if(!authorizationService.isAuthorized(Permissions.EDIT_RECOMMENDATIONS)){
            authorizationService.notAuthorizedAlert();
            return;
        }
//        add movie to recommended
        if (recommendedMovieService.getRecommendedMovieByMovie(currentMovie) == null) {
            recommendedMovieService.addRecommendedMovie(currentMovie);
            recommendationButton.setText("Nie rekomenduj");
        } else {
            recommendedMovieService.deleteRecommendedMovie(currentMovie);
            recommendationButton.setText("Rekomenduj");
        }
        filterMovieService.removeMovieFromFilteredList(currentMovie);
        filterMovieService.addMovieToFilteredList(currentMovie);
    }

    public void deleteMovieAction(ActionEvent actionEvent) {
        if(!authorizationService.isAuthorized(Permissions.REMOVE_MOVIES)){
            authorizationService.notAuthorizedAlert();
            return;
        }
//        show confirmation dialogue
        javafx.scene.control.Alert confirmationAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Potwierdzenie");
        confirmationAlert.setHeaderText("Czy na pewno chcesz usunąć ten film?");
        confirmationAlert.setContentText("Nie będzie można tego cofnąć!");
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                //        safely remove the movie from the database and close the window
                boolean successful =  movieService.deleteMovieById(currentMovie.getId());
//        show error if unsuccessful and successful if successful and close the window (javafx Alert)
                if(successful){
//                    referncedFilteredList.getSource().remove(currentMovie);
                    filterMovieService.removeMovieFromFilteredList(currentMovie);
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Sukces!");
                    alert.setHeaderText("Usunięto film!");
                    alert.setContentText("Usunięto film z bazy danych!");
                    alert.showAndWait();
                    Stage stage = (Stage) titleLabel.getScene().getWindow();
                    stage.close();
                }else{
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Błąd!");
                    alert.setHeaderText("Nie udało się usunąć filmu!");
                    alert.setContentText("Nie udało się usunąć filmu z bazy danych!");
                    alert.showAndWait();
                }
            }
        });

    }

    public void setListReference(FilterMovieService filterMovieService) {
        this.filterMovieService = filterMovieService;
    }

    public void editMovieAction(ActionEvent actionEvent) {
        if(!authorizationService.isAuthorized(Permissions.EDIT_MOVIES)){
            authorizationService.notAuthorizedAlert();
            return;
        }
//        open new dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(titleLabel.getScene().getWindow());
        dialog.setTitle("Edytuj film");

        ButtonType saveButtonType = new ButtonType("Zapisz", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField titleField = new TextField();
        titleField.setPromptText(currentMovie.getName());
        titleField.setText(currentMovie.getName());

        TextField priceField = new TextField();
        priceField.setPromptText(currentMovie.getTicketCost().toString());
        priceField.setText(currentMovie.getTicketCost().toString());
//        make sure that only numbers and dot are allowed and only one dot or comma is allowed
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\.,]\\d*)?")) {
                priceField.setText(oldValue);
            }
        });

        DatePicker releaseDateField = new DatePicker();
        releaseDateField.setPromptText(currentMovie.getReleaseDate().toString());
        releaseDateField.setValue(currentMovie.getReleaseDate().toLocalDate());

        TextField durationField = new TextField();
        durationField.setPromptText(currentMovie.getLength().toString());
        durationField.setText(currentMovie.getLength().toString());
//        make sure that only numbers are allowed
        durationField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                durationField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        ComboBox<Categories> categoryComboBox = new ComboBox<>();
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
//        check if category is set
        if(!Objects.equals(currentMovie.getCategory(), "")){
            categoryComboBox.setValue(Categories.valueOf(currentMovie.getCategory()));
        }

        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText(currentMovie.getImageUrl());
        imageUrlField.setText(currentMovie.getImageUrl());


        grid.add(new Label("Nazwa:"), 0, 0);
        grid.add(titleField, 1, 0);

        grid.add(new Label("Data premiery:"), 0, 1);
        grid.add(releaseDateField, 1, 1);

        grid.add(new Label("Długość:"), 0, 2);
        grid.add(durationField, 1, 2);

        grid.add(new Label("Cena $:"), 0, 3);
        grid.add(priceField, 1, 3);

        grid.add(new Label("URL obrazka:"), 0, 4);
        grid.add(imageUrlField, 1, 4);

        grid.add(new Label("Kategoria:"), 0, 5);
        grid.add(categoryComboBox, 1, 5);


        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().lookupButton(saveButtonType).addEventFilter(ActionEvent.ACTION, event -> {
                    boolean valid = true;
                    if (!imageUrlField.getText().isEmpty()) {
                        try {
                            URL url = new URL(imageUrlField.getText());
                            url.toURI();
                        } catch (MalformedURLException | URISyntaxException e) {
                            valid = false;
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd!");
                            alert.setHeaderText("Niepoprawny URL!");
                            alert.showAndWait();
                            event.consume();
                        }
                    }
                    if (titleField.getText().isEmpty() || releaseDateField.getValue() == null || durationField.getText().isEmpty() || priceField.getText().isEmpty()) {
                        valid = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd!");
                        alert.setHeaderText("Wypełnij wszystkie pola!");
                        alert.showAndWait();
                        event.consume();
                    }
                    if(valid)
                    {
                        currentMovie.setName(titleField.getText());
                        System.out.println("Set name to: "+titleField.getText());
                        currentMovie.setReleaseDate(releaseDateField.getValue().atStartOfDay());
                        currentMovie.setLength(Integer.parseInt(durationField.getText()));
                        currentMovie.setTicketCost(Float.parseFloat(priceField.getText()));
                        Categories resultCategory = FxUtils.getComboBoxValue(categoryComboBox);
                        if(resultCategory == null){
                            currentMovie.setCategory("");
                        } else {
                            currentMovie.setCategory(resultCategory.name());
                        }
                        if(imageUrlField.getText().isEmpty()){
                            currentMovie.applyDefaultUrl();
                        }
                        else{
                            currentMovie.setImageUrl(imageUrlField.getText());
                        }
                        try {
                            movieService.updateMovie(currentMovie);
                            filterMovieService.removeMovieFromFilteredList(currentMovie);
                            filterMovieService.addMovieToFilteredList(currentMovie);
                            updateView();
                        } catch (SQLException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd!");
                            alert.setHeaderText("Nie udało się zaktualizować filmu!");
                            alert.showAndWait();
                        }
//                        edit movie via MovieService
//                        movieService.editMovie(currentMovie.getId(), titleField.getText(),Integer.parseInt(durationField.getText()), releaseDateField.getValue().atStartOfDay(), Float.parseFloat(priceField.getText()), categoryComboBox.getValue(),imageUrlField.getText());

                    }
                });


        dialog.showAndWait();
    }
}
