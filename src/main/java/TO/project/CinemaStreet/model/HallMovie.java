package TO.project.CinemaStreet.model;

import TO.project.CinemaStreet.Roles;
import jakarta.persistence.*;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "hallmovie")
public class HallMovie implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final ObjectProperty<Hall> hall = new SimpleObjectProperty<>(this, "hall");
    private final ObjectProperty<Movie> movie = new SimpleObjectProperty<>(this, "movie");
    private final IntegerProperty seatsTaken = new SimpleIntegerProperty(this, "seatsTaken");
    private final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>(this, "date");


    public HallMovie() {
    }

    public HallMovie(Hall hall, Movie movie, LocalDateTime date) {
        this.hall.set(hall);
        this.movie.set(movie);
        this.date.set(date);
        this.seatsTaken.set(0);
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

    @Column(name ="movieId")
    public final int getMovieId() {
        return movie.get().getId();
    }
    @Column(name ="movieId")
    public final void setMovieId(int movieId) {
        this.movie.get().setId(movieId);
    }

    @Column(name ="hallId")
    public final int getHallId() {
        return hall.get().getId();
    }

    public final Hall getHall() {
        return hall.get();
    }

    public final void setHall(Hall hall) {
        this.hall.set(hall);
    }

    @Column(name ="hallId")
    public final void setHallId(int hallId) {
        this.hall.get().setId(hallId);
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
    public LocalDateTime getDate() {
        return date.get();
    }
    public final ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }
    public final void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public final Integer getSeatsTaken() {
        return seatsTaken.get();
    }
    public IntegerProperty seatsTakenProperty() {
        return seatsTaken;
    }
    public final void setSeatsTaken(Integer seatsTaken) {
        this.seatsTaken.set(seatsTaken);
    }

    public int howManySeatsLeft() {
        return hall.get().getSeatsNumber() - seatsTaken.get();
    }


    @Override
    public String toString() {
        return "HallMovie{" +
                "id=" + id +
                ", seatsTaken=" + seatsTaken+
                ", date=" + date.toString()+
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
        date.set((LocalDateTime) in.readObject());
        seatsTaken.set((int) in.readObject());
    }
}
