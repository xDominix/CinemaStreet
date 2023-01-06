package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.Movie;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final HallService hallService;
    private final HallMovieService hallMovieService;

    public MovieService(MovieRepository movieRepository, HallService hallService, HallMovieService hallMovieService) {
        this.movieRepository = movieRepository;
        this.hallService = hallService;
        this.hallMovieService = hallMovieService;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    public Movie getMovieById(int id){
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.orElse(null);
    }

    public boolean deleteMovieById(Integer id) {
//        remove all hall-movie relations
        if(!hallMovieService.removeAllHallMovieRelationsByMovieId(id)){
            return false;
        }
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

    public List<Movie> getMoviesByCategory(Categories selectedCategory) {
        List<Movie> movies = movieRepository.findAll();
        List<Movie> moviesByCategory = new LinkedList<>();
        for (Movie movie : movies) {
            if (movie.getCategory().equals(selectedCategory.name())) {
                moviesByCategory.add(movie);
            }
        }
        return moviesByCategory;
    }
}
