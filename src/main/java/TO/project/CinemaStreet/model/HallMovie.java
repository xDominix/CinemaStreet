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
public class HallMovie implements Externalizable
{
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final IntegerProperty hallId = new SimpleIntegerProperty(this, "hallid");
    private final IntegerProperty movieId = new SimpleIntegerProperty(this, "movieid");
    private final ObjectProperty<Date> date = new SimpleObjectProperty<>(this, "date");


    public HallMovie() {
    }

    public HallMovie(Hall hall,Movie movie,Date date) {
        this.hallId.set(hall.getId());
        this.movieId.set(movie.getId());
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


    public final Integer getHallId() { return hallId.get();
    }
    public IntegerProperty hallIdProperty() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId.set(hallId);
    }
    public final Integer getMovieId() { return movieId.get();
    }
    public IntegerProperty movieIdProperty() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId.set(movieId);
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
                ", hallid=" + getHallId().toString()+
                ", movieid=" + getMovieId().toString()+
                ", date=" + date.toString()+
                +'}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(id.get());
        out.writeObject(hallId.get());
        out.writeObject(movieId.get());
        out.writeObject(date.get());
    }
    @Override
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        hallId.set((Integer) in.readObject());
        movieId.set((Integer) in.readObject());
        date.set((Date) in.readObject());
    }
}
