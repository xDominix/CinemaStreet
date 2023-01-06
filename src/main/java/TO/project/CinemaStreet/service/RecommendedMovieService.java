package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.model.RecommendedMovie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendedMovieService {
    private final RecommendedMovieRepository recommendedMovieRepository;

    public RecommendedMovieService(RecommendedMovieRepository recommendedMovieRepository) {
        this.recommendedMovieRepository = recommendedMovieRepository;
    }

    public List<RecommendedMovie> getAllRecommendedMovies() {
        return recommendedMovieRepository.findAll();
    }

    public void addRecommendedMovie(RecommendedMovie recommendedMovie) {
        recommendedMovieRepository.save(recommendedMovie);
    }

    public boolean deleteRecommendedMovieById(Integer id) {
        if (recommendedMovieRepository.existsById(id)) {
            recommendedMovieRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public RecommendedMovie getRecommendedMovieByMovie(Movie movie) {
        return recommendedMovieRepository.findByMovieId(movie.getId());
    }

    public RecommendedMovie getRecommendedMovieByPosition(Integer position) {
        return recommendedMovieRepository.findByPosition(position);
    }


    public void removeAllHallMovies() {
        recommendedMovieRepository.deleteAll();
        recommendedMovieRepository.flush();
    }

    public void updateRecommendedMovie(RecommendedMovie recommendedMovie) {
        recommendedMovieRepository.save(recommendedMovie);
    }

}
