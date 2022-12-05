package TO.project.CinemaStreet.model;

public enum Role {
    USER, ADMIN,MODERATOR;

    public Role parseRole(String role) {
        role = role.toUpperCase();
        return switch (role) {
            case "USER" -> USER;
            case "ADMIN" -> ADMIN;
            case "MODERATOR" -> MODERATOR;
            default -> null;
        };

    }
}

