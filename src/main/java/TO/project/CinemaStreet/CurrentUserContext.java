package TO.project.CinemaStreet;

import TO.project.CinemaStreet.service.UserService;
import TO.project.CinemaStreet.utils.AuthenticationResponse;

public class CurrentUserContext {
    private static Integer userId = null;
    private static Roles role = null;

    private static String username = null;
    private static String firstname = null;
    private static String lastname = null;
    private UserService userService = null;

    public CurrentUserContext() {}

    public CurrentUserContext(UserService userService) {
        this.userService = userService;
        this.signOut();
    }

    public void signIn(String login, String password) {
        AuthenticationResponse data = userService.authenticate(login, password);
        if (data.getIsAuthenticated()) {
            userId = data.getUserId();
            username = data.getUsername();
            firstname = data.getFirstname();
            lastname = data.getLastname();
            System.out.println("ej:"+data.getRole());
            this.role = data.getRole();
        }
        else {
            this.signOut();
        }
    }

    public void signOut() {
        userId = null;
         role = null;
    }

    public Roles getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
