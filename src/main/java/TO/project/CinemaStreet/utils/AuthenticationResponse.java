package TO.project.CinemaStreet.utils;

import TO.project.CinemaStreet.Roles;

public final class AuthenticationResponse {
    private final Integer userId;
    private final Roles role;
    private final boolean isAuthenticated;
    private final String username;
    private final String firstname;
    private final String lastname;


    public AuthenticationResponse(Integer userId, Roles role, boolean isAuthenticated, String username, String firstname, String lastname) {
        this.userId = userId;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Integer getUserId() {
        return userId;
    }

    public Roles getRole() {
        return role;
    }

    public boolean getIsAuthenticated() {
        return isAuthenticated;
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
