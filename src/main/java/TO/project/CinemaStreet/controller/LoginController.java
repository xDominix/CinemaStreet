package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class LoginController {
    @Autowired
    private ConfigurableApplicationContext springContext;
    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    public void initialize() {
        updateView();
    }

    private void updateView(){
        //reset inputs
        usernameTextField.setText("");
        passwordTextField.setText("");
    }

    @FXML
    public void signIn(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/MenuView.fxml"));
            loader.setControllerFactory(springContext::getBean);
            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(scene);
            stage.show();

            // get a handle to the stage
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            // do what you have to do
            currentStage.hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
