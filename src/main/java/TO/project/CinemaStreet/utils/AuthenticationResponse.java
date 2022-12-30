package TO.project.CinemaStreet.utils;

import TO.project.CinemaStreet.Roles;

public final class AuthenticationResponse {
    private final Integer userId;
    private final Roles role;
    private final boolean isAuthenticated;


    public AuthenticationResponse(Integer userId, Roles role, boolean isAuthenticated) {
        this.userId = userId;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
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
}
