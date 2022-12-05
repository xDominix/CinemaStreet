package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Cell;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @FXML
    ListView<User> userListView = new ListView<User>();

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        // TODO additional FX controls initialization
        userListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getFirstName() == null) {
                    setText(null);
                } else {
                    setText(item.getFirstName()+" "+item.getLastName()+" "+item.getEmail()+" "+item.getRole());
                }
            }
        });

        ObservableList<User> items =   FXCollections.observableArrayList(userService.getAllUsers());
        userListView.setItems(items);

    }
    public User getUser(int id)
    {

        return null;
    }
//    public List<User> getAllUsers()
//    {
//        return userService.getAllUsers();
//    }
}
