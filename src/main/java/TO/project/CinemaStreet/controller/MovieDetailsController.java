package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.Movie;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@Controller
public class MovieDetailsController {
    @Autowired
    private ConfigurableApplicationContext springContext;
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
