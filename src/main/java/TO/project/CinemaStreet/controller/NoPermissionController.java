package TO.project.CinemaStreet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class NoPermissionController {
    @Autowired
    private ConfigurableApplicationContext springContext;

    @FXML
    private Button closeButton;

    public NoPermissionController() {
    }

    @FXML
    public void initialize() {
        updateView();
    }

    private void updateView() {
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        Stage currentStage = (Stage) closeButton.getScene().getWindow();
        currentStage.hide();
    }
}
