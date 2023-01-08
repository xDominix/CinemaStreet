package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.model.Category;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.AuthorizationService;
import TO.project.CinemaStreet.service.FilterMovieService;
import TO.project.CinemaStreet.service.MovieService;
import TO.project.CinemaStreet.service.RecommendedMovieService;
import TO.project.CinemaStreet.utils.FxUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@Controller
public class MovieController {

    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    FlowPane movieFlowPane;
    @FXML
    ComboBox<Categories> searchByCategoryComboBox;
    @FXML
    private TextField searchTextField;

    @FXML CheckBox searchByRecommmendedCheckBox;

    MovieService movieService;
    private FilterMovieService filterMovieService;

    private RecommendedMovieService recommendedMovieService;
    private AuthorizationService authorizationService;
    public MovieController(MovieService movieService, FilterMovieService filterMovieService, RecommendedMovieService recommendedMovieService, AuthorizationService authorizationService) {
        this.movieService = movieService;
        this.filterMovieService = filterMovieService;
        this.recommendedMovieService = recommendedMovieService;
        this.authorizationService = authorizationService;
    }
    private FilteredList<Movie> filteredList;
    @FXML
    public void initialize() {
        //unfocus pathField
        Platform.runLater( () -> searchTextField.getParent().requestFocus() );

        filteredList = filterMovieService.getFilteredList();
        searchTextField.textProperty().addListener(filterMovieService.getNameInputListener());
        searchByRecommmendedCheckBox.selectedProperty().addListener(filterMovieService.getCheckBoxListener());

        movieFlowPane.setHgap(20);
        movieFlowPane.setVgap(20);
//        insert movies into flowpane
        initializeMoviePane(filteredList);
        setMoviesToPane(filteredList);

//        initialize category combobox
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

        searchByCategoryComboBox.valueProperty().addListener(filterMovieService.getCategoryInputListener());
    }


    private void setMoviesToPane(List<Movie> movies){
        if(movieFlowPane == null){
            System.out.println("movieFlowPane is null");
            return;
        }
        movieFlowPane.getChildren().clear();
        for (Movie movie :
                movies) {
            VBox movieCard = createMovieCard(movie);
            movieFlowPane.getChildren().add(movieCard);
        }
    }

    private void initializeMoviePane(FilteredList<Movie> movies){
        movies.addListener((ListChangeListener<Movie>) c -> {
//            TODO: should be optimized
            setMoviesToPane(movies);
        });
    }

    private VBox createMovieCard(Movie movie){
        Image image = new Image(movie.getImageUrl());
        ImageView imageView = new ImageView(image);
        float multiplier = 0.8f;
        imageView.setFitHeight(450*multiplier);
        imageView.setFitWidth(300*multiplier);


        Label movieTitle = new Label(movie.getName());
        movieTitle.setPadding(new Insets(0,0,0,0));
        movieTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;-fx-cursor: hand;");
        VBox movieCard = new VBox(imageView, movieTitle);
        movieCard.setPadding(new Insets(10,10,10,10));
        movieCard.setSpacing(0);
        movieCard.setAlignment(javafx.geometry.Pos.CENTER);
        if(recommendedMovieService.isRecommended(movie)){
            movieCard.setStyle("-fx-background-color: #6a7d63;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");
        }else{
            movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");
        }


//        set on hover
        movieCard.setOnMouseEntered(event -> {
            if(recommendedMovieService.isRecommended(movie)){
                movieCard.setStyle("-fx-background-color: #6a7d63;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, #ffffff, 10, 0, 0, 0);");
            }else{
                movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, #ffffff, 10, 0, 0, 0);");
            }

        });
        movieCard.setOnMouseExited(event -> {
            if(recommendedMovieService.isRecommended(movie)){
                movieCard.setStyle("-fx-background-color: #6a7d63;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");
            }else{
                movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");
            }
        });
//        set on click event
        movieCard.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MovieDetailsView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 500, 700);
                MovieDetailsController movieDetailsController = loader.getController();
                movieDetailsController.setMovie(movie);
                movieDetailsController.setListReference(filterMovieService);
                Stage stage = new Stage();
                stage.setTitle(movie.getName());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        return movieCard;
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

    public void addNewMovieAction(ActionEvent actionEvent) {
        if(!authorizationService.isAuthorized(Permissions.ADD_MOVIES)){
            authorizationService.notAuthorizedAlert();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Dodaj film");

        ButtonType okButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));


        TextField titleField = new TextField();
        titleField.setPromptText("Nazwa");

        TextField priceField = new TextField();
        priceField.setPromptText("0");
//        make sure that only numbers and dot are allowed and only one dot or comma is allowed
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*([\\.,]\\d*)?")) {
                priceField.setText(oldValue);
            }
        });

        DatePicker releaseDateField = new DatePicker();
        releaseDateField.setPromptText("Wybierz datę premiery");

        TextField durationField = new TextField();
        durationField.setPromptText("0");
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


        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("URL obrazka");


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
        dialog.getDialogPane().lookupButton(okButtonType).addEventFilter(ActionEvent.ACTION, event -> {
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
            if(valid){
                Movie movie;
                Categories resultCategory = FxUtils.getComboBoxValue(categoryComboBox);
//                cover the case where url is empty and/or category is null (lol, gdzie builder)
                if (imageUrlField.getText().isEmpty() && resultCategory == null) {
                    movie = new Movie(titleField.getText(),Integer.parseInt(durationField.getText()), releaseDateField.getValue().atStartOfDay(), Float.parseFloat(priceField.getText()));
                }
                else if (imageUrlField.getText().isEmpty()) {
                    movie = new Movie(titleField.getText(),Integer.parseInt(durationField.getText()), releaseDateField.getValue().atStartOfDay(), Float.parseFloat(priceField.getText()), resultCategory);
                }
                else if (resultCategory == null) {
                    movie = new Movie(titleField.getText(),Integer.parseInt(durationField.getText()), releaseDateField.getValue().atStartOfDay(), Float.parseFloat(priceField.getText()), imageUrlField.getText());
                }
                else {
                    movie = new Movie(titleField.getText(),Integer.parseInt(durationField.getText()), releaseDateField.getValue().atStartOfDay(), Float.parseFloat(priceField.getText()), resultCategory,imageUrlField.getText());
                }

                filterMovieService.addMovieToFilteredList(movie);
                movieService.addMovie(movie);
            }
        });
        dialog.showAndWait();
    }
}
