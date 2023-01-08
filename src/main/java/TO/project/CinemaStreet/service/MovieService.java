package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Categories;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.Movie;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final HallService hallService;
    private final HallMovieService hallMovieService;
    private final RecommendedMovieService recommendedMovieService;

    public MovieService(MovieRepository movieRepository, HallService hallService, HallMovieService hallMovieService, RecommendedMovieService recommendedMovieService) {
        this.movieRepository = movieRepository;
        this.hallService = hallService;
        this.hallMovieService = hallMovieService;
        this.recommendedMovieService = recommendedMovieService;
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
//        remove all recommended-movie relations
        if(!recommendedMovieService.removeAllRecommendedMovieRelationsByMovieId(id)){
            return false;
        }
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void removeAllMovies() {
//        remove all recommended movies relations
        recommendedMovieService.removeAllRecommendedMovies();
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

    public void editMovie(int id, String text, int parseInt, LocalDateTime releaseDate, float price, Categories category, String imageUrl) {
        Movie movie = getMovieById(id);
        movie.setName(text);
        movie.setLength(parseInt);
        movie.setReleaseDate(releaseDate);
        movie.setTicketCost(price);
        movie.setCategory(category.name());
        movie.setImageUrl(imageUrl);
        movieRepository.save(movie);
    }

    public void updateMovie(Movie currentMovie)throws SQLException {
        movieRepository.save(currentMovie);
    }
}
