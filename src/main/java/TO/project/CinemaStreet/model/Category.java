package TO.project.CinemaStreet.model;

import jakarta.persistence.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Entity
@Table(name = "categories")
public class Category implements Externalizable {

    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name");

    public Category() {
    }

    public Category(Integer id, String name) {
        this.id.set(id);
        this.name.set(name);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public final int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public final void setId(Integer id) {
        this.id.set(id);
    }

    public final String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }
    public final void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id.get());
        out.writeObject(name.get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        name.set((String) in.readObject());
    }
}
