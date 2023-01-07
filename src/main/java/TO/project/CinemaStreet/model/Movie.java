package TO.project.CinemaStreet.model;

import TO.project.CinemaStreet.Categories;
import jakarta.persistence.*;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "movies")
public class Movie implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final StringProperty name = new SimpleStringProperty(this, "name");
    private final IntegerProperty length = new SimpleIntegerProperty(this, "length");
    private final ObjectProperty<LocalDateTime> releaseDate = new SimpleObjectProperty<>(this, "releaseDate");
    private final FloatProperty ticketCost = new SimpleFloatProperty(this, "ticketCost");
    private final StringProperty imageUrl = new SimpleStringProperty(this, "imageUrl");
    private final StringProperty category = new SimpleStringProperty(this, "category");

    public Movie() {
    }
    //probably should be replaced by builder pattern
    public Movie(String name, Integer length, LocalDateTime releaseDate, Float ticketCost) {
        this.name.set(name);
        this.length.set(length);
        this.releaseDate.set(releaseDate);
        this.ticketCost.set(ticketCost);
        this.category.set("");
        this.imageUrl.set("https://posters.movieposterdb.com/20_01/2017/7131440/l_7131440_be0c6b24.jpg");
    }
    public Movie(String name, Integer length, LocalDateTime releaseDate, Float ticketCost, Categories category) {
        this.name.set(name);
        this.length.set(length);
        this.releaseDate.set(releaseDate);
        this.ticketCost.set(ticketCost);
        this.category.set(category.name());
        this.imageUrl.set("https://posters.movieposterdb.com/20_01/2017/7131440/l_7131440_be0c6b24.jpg");
    }
    public Movie(String name, Integer length, LocalDateTime releaseDate, Float ticketCost, String imageUrl) {
        this.name.set(name);
        this.length.set(length);
        this.releaseDate.set(releaseDate);
        this.ticketCost.set(ticketCost);
        this.category.set("");
        this.imageUrl.set(imageUrl);
    }
    public Movie(String name, Integer length, LocalDateTime releaseDate, Float ticketCost, Categories category, String imageUrl) {
        this.name.set(name);
        this.length.set(length);
        this.releaseDate.set(releaseDate);
        this.ticketCost.set(ticketCost);
        this.category.set(category.name());
        this.imageUrl.set(imageUrl);
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


    public final String getCategory() {
        return category.get();
    }
    public StringProperty categoryProperty() {
        return category;
    }
    public final void setCategory(String category) {
        this.category.set(category);
    }

    public final String getImageUrl() {
        return imageUrl.get();
    }
    public StringProperty imageUrlProperty() {
        return imageUrl;
    }
    public final void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
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

    public final LocalDateTime getReleaseDate() {
        return releaseDate.get();
    }
    public ObjectProperty<LocalDateTime> releaseDateProperty() {
        return releaseDate;
    }
    public final void setReleaseDate(LocalDateTime releaseDate) {
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
        releaseDate.set((LocalDateTime) in.readObject());
        ticketCost.set((Float) in.readObject());
    }

    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Movie)) return false;
        Movie movie = (Movie) obj;
        return movie.getId() == this.getId();
    }

    public void applyDefaultUrl() {
        this.imageUrl.set("https://posters.movieposterdb.com/20_01/2017/7131440/l_7131440_be0c6b24.jpg");
    }
}
