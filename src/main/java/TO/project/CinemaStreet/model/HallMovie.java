package TO.project.CinemaStreet.model;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.service.HallService;
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
    private final IntegerProperty hallId = new SimpleIntegerProperty(this, "hallId");
    private final IntegerProperty movieId = new SimpleIntegerProperty(this, "movieId");
    private final IntegerProperty seatsTaken = new SimpleIntegerProperty(this, "seatsTaken");
    private final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>(this, "date");

    public HallMovie() {
    }

    public HallMovie(Hall hall, Movie movie, LocalDateTime date) {
        this.hallId.set(hall.getId());
        this.movieId.set(movie.getId());
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
        return movieId.get();
    }
    @Column(name ="movieId")
    public final void setMovieId(int movieId) {
        this.movieId.set(movieId);
    }

    @Column(name ="hallId")
    public final int getHallId() {
        return hallId.get();
    }
    @Column(name ="hallId")
    public final void setHallId(int hallId) {
        this.hallId.set(hallId);
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

    @Override
    public String toString() {
        return "HallMovie{" +
                "id=" + id +
                ", seatsTaken=" + seatsTaken+
                ", date=" + date+
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
        hallId.set((int) in.readObject());
        movieId.set((int) in.readObject());
        date.set((LocalDateTime) in.readObject());
        seatsTaken.set((int) in.readObject());
    }
}
