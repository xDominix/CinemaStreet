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

    // TODO Check what position is?
    public void addRecommendedMovie(Movie movie) {
        recommendedMovieRepository.save(new RecommendedMovie(movie, 10));
    }

    public void deleteRecommendedMovie(Movie movie) {
        recommendedMovieRepository.deleteByMovieId(movie.getId());
    }

    public boolean isRecommended(Movie movie) {
        return recommendedMovieRepository.existsByMovieId(movie.getId());
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

    public void removeAllRecommendedMovies() {
        recommendedMovieRepository.deleteAll();
    }

    public boolean removeAllRecommendedMovieRelationsByMovieId(Integer id) {
        if (recommendedMovieRepository.existsByMovieId(id)) {
            recommendedMovieRepository.deleteByMovieId(id);
            return true;
        }
        return false;
    }
}
