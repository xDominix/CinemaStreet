package TO.project.CinemaStreet.controller;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.service.UserService;
import TO.project.CinemaStreet.utils.NoPermissionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class UserController {
    @Autowired
    private ConfigurableApplicationContext springContext;

    @FXML
    ListView<User> userListView = new ListView<User>();

    @FXML
    private TextField idTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private RadioButton userRadioButton;

    @FXML
    private RadioButton adminRadioButton;
    @FXML
    private RadioButton moderatorRadioButton;

    final ToggleGroup group = new ToggleGroup();

    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void initialize() {
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
        usernameTextField.setText("");
        passwordPasswordField.setText("");
        firstnameTextField.setText("");
        lastNameTextField.setText("");
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
        try {
            if(checkInputs())
            {
                System.out.println(((RadioButton)group.getSelectedToggle()).getText());
                User user = new User(usernameTextField.getText(), passwordPasswordField.getText(), firstnameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), Roles.valueOf(((RadioButton)group.getSelectedToggle()).getText()));
                userService.addUser(user);
                updateView();
            }
        } catch(NoPermissionException e) {
            this.throwError();
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
            catch(NoPermissionException e) {
                this.throwError();
            }
    }

    private boolean checkInputs(){
        removeInputsErrors();

        if(usernameTextField.getText().length()<3)
        {
            usernameTextField.getStyleClass().add("error");
            return false;
        }

        if(passwordPasswordField.getText().length()<3)
        {
            passwordPasswordField.getStyleClass().add("error");
            return false;
        }

        if(firstnameTextField.getText().length()<3)
        {
            firstnameTextField.getStyleClass().add("error");
            return false;
        }

        if(lastNameTextField.getText().length()<3) {
            lastNameTextField.getStyleClass().add("error");
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
        usernameTextField.getStyleClass().remove("error");
        passwordPasswordField.getStyleClass().remove("error");
        firstnameTextField.getStyleClass().remove("error");
        lastNameTextField.getStyleClass().remove("error");
        emailTextField.getStyleClass().remove("error");
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
