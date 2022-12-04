package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.Role;
import TO.project.CinemaStreet.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class UserService {
    public static ObservableList<User> getAllUsers()
    {
        return FXCollections.observableArrayList(new User("Jan", "Kowalski", "email@gmail.com", Role.USER));
    }
}
