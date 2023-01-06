package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.service.MovieService;
import TO.project.CinemaStreet.utils.FxUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;


@Controller
public class MovieController {

    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    FlowPane movieFlowPane;
    @FXML
    ComboBox<Categories> searchByCategoryComboBox;

    MovieService movieService;
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @FXML
    public void initialize() {
        movieFlowPane.setHgap(20);
//        insert movies into flowpane
        List<Movie> movies = movieService.getAllMovies();
        setMoviesToPane(movies);

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
//        set on action for category combobox
        searchByCategoryComboBox.setOnAction(event -> {
            Categories selectedCategory = FxUtils.getComboBoxValue(searchByCategoryComboBox);
            System.out.println(selectedCategory);
            if(selectedCategory == null){
                setMoviesToPane(movies);
                return;
            }
            List<Movie> moviesByCategory = movieService.getMoviesByCategory(selectedCategory);
            setMoviesToPane(moviesByCategory);

        });
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
        movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");

//        set on hover
        movieCard.setOnMouseEntered(event -> {
            movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, #ffffff, 10, 0, 0, 0);");
        });
        movieCard.setOnMouseExited(event -> {
            movieCard.setStyle("-fx-background-color: #4e4e4e;-fx-border-radius: 5px; -fx-border-color: #2D2D30n; -fx-border-width: 2px; -fx-cursor: hand;");
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


}
