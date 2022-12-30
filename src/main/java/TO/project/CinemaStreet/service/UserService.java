package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.User;
import TO.project.CinemaStreet.utils.AuthenticationResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticate(String username, String password){
        if (userRepository.existsByUsernameAndPassword(username, password)) {
            User user = userRepository.getReferenceByUsernameAndPassword(username, password);
            return new AuthenticationResponse(user.getId(), user.getRole(), true);
        }
        return new AuthenticationResponse(0, Roles.EMPLOYEE, false);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
//        return FXCollections.observableArrayList(new User("Jan", "Kowalski", "email@gmail.com", Role.USER));
    }
    public void addUser(User user)
    {
        userRepository.save(user);
    }

    public boolean deleteUserById(Integer id){
        if(userRepository.existsById(id))
        {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
