package TO.project.CinemaStreet.model;

import jakarta.persistence.*;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Date;

@Entity
@Table(name = "movies")
public class Movie implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final IntegerProperty length = new SimpleIntegerProperty(this, "length");
    private final ObjectProperty<Date> releaseDate = new SimpleObjectProperty<>(this, "releaseDate");
    private final FloatProperty ticketCost = new SimpleFloatProperty(this, "ticketCost");

    public Movie(String name, Integer length, Date releaseDate, Float ticketCost) {
        this.name.set(name);
        this.length.set(length);
        this.releaseDate.set(releaseDate);
        this.ticketCost.set(ticketCost);
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

    public final Integer getLength() {
        return length.get();
    }
    public IntegerProperty lengthProperty() {
        return length;
    }
    public final void setLength(Integer length) {
        this.length.set(length);
    }

    public final Date getReleaseDate() {
        return releaseDate.get();
    }
    public ObjectProperty<Date> releaseDateProperty() {
        return releaseDate;
    }
    public final void setReleaseDate(Date releaseDate) {
        this.releaseDate.set(releaseDate);
    }

    public final Float getTicketCost() {
        return ticketCost.get();
    }
    public FloatProperty ticketCostProperty() {
        return ticketCost;
    }
    public final void setTicketCost(Float ticketCost) {
        this.ticketCost.set(ticketCost);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id.get());
        out.writeObject(name.get());
        out.writeObject(length.get());
        out.writeObject(releaseDate.get());
        out.writeObject(ticketCost.get());
    }

    @Override
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        name.set((String) in.readObject());
        length.set((Integer) in.readObject());
        releaseDate.set((Date) in.readObject());
        ticketCost.set((Float) in.readObject());
    }
}
