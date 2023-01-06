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
    private Label categoryLabel;
    @FXML
    public void initialize() {
//        placeholder image in case of no image
        imageView.setImage(new javafx.scene.image.Image("https://posters.movieposterdb.com/20_01/2017/7131440/l_7131440_be0c6b24.jpg"));
    }
    public void setMovie(Movie movie) {
        currentMovie = movie;
        updateView();
    }
    private void updateView() {
        imageView.setImage(new javafx.scene.image.Image(currentMovie.getImageUrl()));
        titleLabel.setText(currentMovie.getName());
        idLabel.setText(String.valueOf(currentMovie.getId()));
        categoryLabel.setText(currentMovie.getCategory());
//        if category empty then set NIE USTAWIONO
        if(currentMovie.getCategory() == ""){
            categoryLabel.setText("NIE USTAWIONO");
        }
//        TODO: rest of the fields
//        TODO: make it look good
    }
}
