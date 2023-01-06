package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.RecommendedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendedMovieRepository extends JpaRepository<RecommendedMovie, Integer> {
    RecommendedMovie findByMovieId(int movieId);
    RecommendedMovie findByPosition(int position);
}
