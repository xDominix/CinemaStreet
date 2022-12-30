package TO.project.CinemaStreet.model;

import jakarta.persistence.*;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

@Entity
@Table(name = "halls")
public class Hall implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final IntegerProperty seatsNumber = new SimpleIntegerProperty(this, "seatsNumber");

    public Hall() {
    }

    public Hall(Integer id,Integer seatsNumber) {
        this.id.set(id);
        this.seatsNumber.set(seatsNumber);
    }

    @Id
//    @GeneratedValue(strategy = GenerationType.TABLE)
    public final int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public final void setId(Integer id) {
        this.id.set(id);
    }

    public final Integer getSeatsNumber() {
        return seatsNumber.get();
    }
    public IntegerProperty seatsNumberProperty() {
        return seatsNumber;
    }
    public final void setSeatsNumber(Integer seatsNumber) {
        this.seatsNumber.set(seatsNumber);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id.get());
        out.writeObject(seatsNumber.get());
    }
    @Override
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        seatsNumber.set((Integer) in.readObject());
    }
    public String toString()
    {
        return "Hall: " + id.get() + " seats: " + seatsNumber.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Hall.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Hall other = (Hall) obj;
        if (this.id.get() != other.id.get()) {
            return false;
        }
        return true;
    }
}
