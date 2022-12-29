package TO.project.CinemaStreet;

import java.util.Arrays;

public enum Roles {
    EMPLOYEE("EMPLOYEE"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN");

    private final String role;

    final Permissions[] employee = {
            Permissions.VIEW_USERS
    };
    final Permissions[] moderator = {
            Permissions.VIEW_USERS
    };
    final Permissions[] admin = {
            Permissions.VIEW_USERS,
            Permissions.ADD_USER,
            Permissions.EDIT_USER,
            Permissions.REMOVE_USER
    };

    Roles(String role) {
        this.role = role;
    }

    public boolean authorize(String role,  Permissions permission) {
        return switch (role) {
            case "EMPLOYEE" -> Arrays.asList(employee).contains(permission);
            case "MODERATOR" -> Arrays.asList(moderator).contains(permission);
            case "ADMIN" -> Arrays.asList(admin).contains(permission);
            default -> false;
        };

    }

    @Override
    public String toString() {
        return role;
    }
}