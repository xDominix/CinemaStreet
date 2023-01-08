package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.CurrentUserContext;
import TO.project.CinemaStreet.Permissions;
import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.utils.AuthenticationResponse;
import TO.project.CinemaStreet.utils.NoPermissionException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{
    private final UserRepository userRepository;
    private CurrentUserContext currentUserContext = new CurrentUserContext();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticate(String username, String password){
        if (userRepository.existsByUsernameAndPassword(username, password)) {
            User user = userRepository.getReferenceByUsernameAndPassword(username, password);
            System.out.println("User: " + user.getRole());
            return new AuthenticationResponse(user.getId(), user.getRole(), true, user.getUsername(), user.getFirstName(), user.getLastName());
        }
        return new AuthenticationResponse(0, Roles.EMPLOYEE, false, "", "", "");
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
//        return FXCollections.observableArrayList(new User("Jan", "Kowalski", "email@gmail.com", Role.USER));
    }
    public void addUser(User user) throws NoPermissionException {
        if (currentUserContext.getRole().authorize(Permissions.ADD_USER)) {
            userRepository.save(user);
        } else {
            throw new NoPermissionException("No permission");
        }
    }

    public boolean deleteUserById(Integer id) throws NoPermissionException {
        if (currentUserContext.getRole().authorize(Permissions.REMOVE_USER)) {
            if(userRepository.existsById(id))
            {
                userRepository.deleteById(id);
                return true;
            }
            return false;
        } else {
            throw new NoPermissionException("No permission");
        }

    }
}
