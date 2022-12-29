package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Roles;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {

    @FXML
    ListView<User> userListView = new ListView<User>();

    @FXML
    private TextField idTextField;

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
                    setText(item.getId()+" "+item.getFirstName()+" "+item.getLastName()+" "+item.getEmail()+" "+item.getRole());
                }
            }
        });

        userRadioButton.setToggleGroup(group);
        userRadioButton.setSelected(true);

        adminRadioButton.setToggleGroup(group);

        moderatorRadioButton.setToggleGroup(group);

        updateView();
    }
    private void updateView(){
        //reset inputs
        nameTextField.setText("");
        LastNameTextField.setText("");
        emailTextField.setText("");
        userRadioButton.setSelected(true);
        idTextField.setText("");

        //update list
        ObservableList<User> items =   FXCollections.observableArrayList(userService.getAllUsers());
        userListView.setItems(items);
    }

    public User getUser(int id)
    {
        //TODO
        return null;
    }
    @FXML
    private void addNewUser(ActionEvent actionEvent) {
        if(checkInputs())
        {
            System.out.println(((RadioButton)group.getSelectedToggle()).getText());
            User user = new User(nameTextField.getText(), LastNameTextField.getText(), emailTextField.getText(), Roles.valueOf(((RadioButton)group.getSelectedToggle()).getText()));
            userService.addUser(user);
            updateView();
        }
    }

    @FXML
    private void deleteUser(ActionEvent actionEvent) {
        removeInputsErrors();
        try{
            boolean deleted = userService.deleteUserById(Integer.parseInt(idTextField.getText()));

            if(!deleted){
                idTextField.getStyleClass().add("error");
                System.out.println("User does not exists");
                return;
            }

            System.out.println("Deleted");
            updateView();
        }
        catch (NumberFormatException ex){
            idTextField.getStyleClass().add("error");
            System.out.println("Input is NaN");
        }

    }

    private boolean checkInputs(){
        removeInputsErrors();

        if(nameTextField.getText().length()<3)
        {
            nameTextField.getStyleClass().add("error");
            return false;
        }

        if(LastNameTextField.getText().length()<3) {
            LastNameTextField.getStyleClass().add("error");
            return false;
        }

        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        Matcher matcher = pattern.matcher(emailTextField.getText());
        if(!matcher.matches()) {
            emailTextField.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    private void removeInputsErrors(){
        idTextField.getStyleClass().remove("error");
        nameTextField.getStyleClass().remove("error");
        LastNameTextField.getStyleClass().remove("error");
        emailTextField.getStyleClass().remove("error");
    }

}
