package TO.project.CinemaStreet.model;

import TO.project.CinemaStreet.Roles;
import jakarta.persistence.*;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Date;

@Entity
@Table(name = "hallmovie")
public class HallMovie implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final ObjectProperty<Hall> hall = new SimpleObjectProperty<>(this, "hall");
    private final ObjectProperty<Movie> movie = new SimpleObjectProperty<>(this, "movie");
    private final ObjectProperty<Date> date = new SimpleObjectProperty<>(this, "date");


    public HallMovie() {
    }

    public HallMovie(Hall hall,Movie movie,Date date) {
        this.hall.set(hall);
        this.movie.set(movie);
        this.date.set(date);
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public final int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public final void setId(Integer id) {
        this.id.set(id);
    }


    public final Hall getHall() {
        return hall.get();
    }
    public ObjectProperty<Hall> hallProperty() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall.set(hall);
    }
    public final Movie getMovie() {
        return movie.get();
    }
    public ObjectProperty<Movie> movieProperty() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie.set(movie);
    }
    public Date getDate() {
        return date.get();
    }
    public final ObjectProperty<Date> dateProperty() {
        return date;
    }
    public final void setDate(Date date) {
        this.date.set(date);
    }


    @Override
    public String toString() {
        return "HallMovie{" +
                "id=" + id +
                ", date=" + date.toString()
                +'}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(id.get());
        out.writeObject(hall.get());
        out.writeObject(movie.get());
        out.writeObject(date.get());
    }
    @Override
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        hall.set((Hall) in.readObject());
        movie.set((Movie) in.readObject());
        date.set((Date) in.readObject());
    }
}
