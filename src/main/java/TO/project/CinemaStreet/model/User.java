package TO.project.CinemaStreet.model;

import TO.project.CinemaStreet.Roles;
import jakarta.persistence.*;
import javafx.beans.property.*;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

@Entity
@Table(name = "users")
public class User implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty username = new SimpleStringProperty(this, "username");
    private final StringProperty password = new SimpleStringProperty(this, "password");
    private final StringProperty firstName = new SimpleStringProperty(this, "firstName");
    private final StringProperty lastName = new SimpleStringProperty(this, "lastName");
    private final StringProperty email = new SimpleStringProperty(this, "email");
    private final ObjectProperty<Roles> role = new SimpleObjectProperty<>(this, "role");

    public User() {
    }

    public User(String username, String password, String firstName, String lastName, String email, Roles role) {
        this.username.set(username);
        this.password.set(password);
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
        this.role.set(role);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public final int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public final void setId(Integer id) {
        this.id.set(id);
    }
    @Column(unique=true)
    public final String getUsername() {
        return username.get();
    }
    public StringProperty usernameProperty() {
        return username;
    }
    public final void setUsername(String username) {
        this.username.set(username);
    }

    public final String getPassword() {
        return password.get();
    }
    public StringProperty passwordProperty() {
        return password;
    }
    public final void setPassword(String password) {
        this.password.set(password);
    }

    public final String getFirstName() {
        return firstName.get();
    }
    public StringProperty firstNameProperty() {
        return firstName;
    }
    public final void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }
    public final StringProperty lastNameProperty() {
        return lastName;
    }
    public final void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public final String getEmail() {
        return email.get();
    }
    public StringProperty emailProperty() {
        return email;
    }
    public final void setEmail(String email) {
        this.email.set(email);
    }

    public Roles getRole() {
        return role.get();
    }
    public ObjectProperty<Roles> roleProperty() {
        return role;
    }
    public void setRole(Roles role) {
        this.role.set(role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id.get() +
                ", firstName=" + firstName.get() +
                ", lastName=" + lastName.get() +
                ", email=" + email.get() +
                ", role=" + role.get() +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id.get());
        out.writeObject(firstName.get());
        out.writeObject(lastName.get());
        out.writeObject(email.get());
        out.writeObject(role.get());
    }
    @Override
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        firstName.set((String) in.readObject());
        lastName.set((String) in.readObject());
        email.set((String) in.readObject());
        role.set((Roles) in.readObject());
    }
}
