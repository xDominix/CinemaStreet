package TO.project.CinemaStreet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import javafx.beans.property.*;


import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

@Entity
public class User implements Externalizable {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id");
    private SimpleStringProperty firstName = new SimpleStringProperty(this, "firstName");
    private SimpleStringProperty lastName = new SimpleStringProperty(this, "lastName");
    private SimpleStringProperty email = new SimpleStringProperty(this, "email");
    private SimpleObjectProperty<Role> role = new SimpleObjectProperty<>(this, "role");

    public User() {
    }

    public User(String firstName, String lastName, String email, Role role) {
        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.email.set(email);
        this.role.set(role);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public Role getRole() {
        return role.get();
    }

    public ObjectProperty<Role> roleProperty() {
        return role;
    }

    public void setRole(Role role) {
        this.role.set(role);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", role=" + role +
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
        role.set((Role) in.readObject());
    }


}
