package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Controller;


@Controller
public class MovieDetailsController {
    private Movie currentMovie;

    @FXML
    private ImageView imageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label idLabel;
    @FXML
    public void initialize() {
        imageView.setImage(new javafx.scene.image.Image("https://cdn.shopify.com/s/files/1/0057/3728/3618/products/interstellar5_480x.progressive.jpg?v=1585846879"));
    }
    public void setMovie(Movie movie) {
        currentMovie = movie;
        updateView();
    }
    private void updateView() {
        titleLabel.setText(currentMovie.getName());
        idLabel.setText(String.valueOf(currentMovie.getId()));
    }
}
