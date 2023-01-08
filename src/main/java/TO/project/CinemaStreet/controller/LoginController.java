package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
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
    private PasswordField passwordTextField;

    @FXML
    private Button signInButton;

    private UserService userService;
    private CurrentUserContext currentUserContext;


    public LoginController(UserService userService) {
        this.userService = userService;
        this.currentUserContext = new CurrentUserContext(userService);
    }

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
        currentUserContext.signIn(usernameTextField.getText(), passwordTextField.getText());
        if (currentUserContext.getRole() != null) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MenuView.fxml"));
                loader.setControllerFactory(springContext::getBean);
                Scene scene = new Scene(loader.load(), 755, 640);
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
        } else {
            this.throwError();
        }
    }

    public void throwError() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/InvalidDataView.fxml"));
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
