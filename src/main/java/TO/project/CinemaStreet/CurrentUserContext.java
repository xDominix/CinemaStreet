package TO.project.CinemaStreet;

import TO.project.CinemaStreet.service.UserService;
import TO.project.CinemaStreet.utils.AuthenticationResponse;

public class CurrentUserContext {
    private Integer userId = null;
    private Roles role = null;
    private final UserService userService;

    CurrentUserContext(UserService userService) {
        this.userService = userService;
        this.signOut();
    }

    public void signIn(String username, String password) {
        AuthenticationResponse data = userService.authenticate(username, password);
        if (data.getIsAuthenticated()) {
            this.userId = data.getUserId();
            this.role = data.getRole();
        }
        else {
            this.signOut();
        }
    }

    public void signOut() {
        this.userId = null;
        this. role = null;
    }

    public Roles getRole() {
        return this.role;
    }
}
