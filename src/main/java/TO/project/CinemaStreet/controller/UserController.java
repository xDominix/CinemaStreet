package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.model.Role;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {

    @FXML
    ListView<User> userListView = new ListView<User>();

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField LastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private RadioButton userRadioButton;

    @FXML
    private RadioButton adminRadioButton;
    @FXML
    private RadioButton moderatorRadioButton;
    @FXML
    private Button addUserButton;
    final ToggleGroup group = new ToggleGroup();



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

        userRadioButton.setToggleGroup(group);
        userRadioButton.setSelected(true);

        adminRadioButton.setToggleGroup(group);

        moderatorRadioButton.setToggleGroup(group);

    }
    public User getUser(int id)
    {
        //TODO
        return null;
    }
    @FXML
    private void addNewUser(ActionEvent actionEvent) {
//        TODO Check if data is correct
        System.out.println(((RadioButton)group.getSelectedToggle()).getText());
        User user = new User(nameTextField.getText(), LastNameTextField.getText(), emailTextField.getText(), Role.valueOf(((RadioButton)group.getSelectedToggle()).getText()));
        userService.addUser(user);
        userListView.getItems().add(user);
    }
}
