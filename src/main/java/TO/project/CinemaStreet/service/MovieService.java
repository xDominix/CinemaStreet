package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public boolean deleteMovieById(Integer id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void removeAllMovies() {
        movieRepository.deleteAll();
    }

    public void addMovies(List<Movie> newMovies) {
        for (Movie movie : newMovies) {
            addMovie(movie);
        }
    }
}
