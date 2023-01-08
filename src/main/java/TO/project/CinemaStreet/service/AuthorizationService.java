package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.Permissions;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private CurrentUserContext currentUserContext;

    public AuthorizationService() {
        this.currentUserContext = new CurrentUserContext();
    }

    public boolean isAuthorized(Permissions permission) {
        System.out.println("Current user role: " + currentUserContext.getFirstname());
        return currentUserContext.getRole().authorize(permission);
    }

    public void notAuthorizedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Brak uprawnień");
        alert.setHeaderText("Brak uprawnień");
        alert.setContentText("Nie masz uprawnień do wykonania tej operacji");
        alert.showAndWait();
    }
}
