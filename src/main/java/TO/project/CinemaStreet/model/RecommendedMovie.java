package TO.project.CinemaStreet.model;

import jakarta.persistence.*;
import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutput;

@Entity
@Table(name = "recommendedmovies")
public class RecommendedMovie implements Externalizable {
    private static final long serialVersionUID = 1L;
    private final IntegerProperty id = new SimpleIntegerProperty(this, "id");
    private final ObjectProperty<Movie> movie = new SimpleObjectProperty<>(this, "movie");
    private final IntegerProperty position = new SimpleIntegerProperty(this, "position");


    public RecommendedMovie() {
    }

    public RecommendedMovie(Movie movie, Integer position) {
        this.movie.set(movie);
        this.position.set(position);
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

    @OneToOne
    @JoinColumn(name = "movieId")
    public final Movie getMovie() {
        return movie.get();
    }
    public final void setMovie(Movie movie) {
        this.movie.set(movie);
    }

//    @Column(name ="movieId")
//    public final int getMovieId() {
//        return movie.get().getId();
//    }
//    @Column(name ="movieId")
//    public final void setMovieId(int movieId) {
//        this.movie.get().setId(movieId);
//    }

    public final Integer getPosition() {
        return position.get();
    }
    public IntegerProperty positionProperty() {
        return position;
    }
    public final void setPosition(Integer seatsTaken) {
        this.position.set(seatsTaken);
    }

    @Override
    public String toString() {
        return "RecommendedMovie{" +
                "id=" + id.get() +
                ", position=" + position.get()+
                +'}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(id.get());
        out.writeObject(movie.get());
        out.writeObject(position.get());
    }
    @Override
    public void readExternal(java.io.ObjectInput in) throws IOException, ClassNotFoundException {
        id.set(in.readInt());
        movie.set((Movie) in.readObject());
        position.set((int) in.readObject());
    }
}
