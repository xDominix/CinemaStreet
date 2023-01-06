package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public void copyUrlAction(ActionEvent actionEvent){
//        copy url of the movie to the system clipboard
        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
        content.putString(currentMovie.getImageUrl());
        clipboard.setContent(content);
    }
}
