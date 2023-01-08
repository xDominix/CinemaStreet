package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.RecommendedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RecommendedMovieRepository extends JpaRepository<RecommendedMovie, Integer> {
    @Query("SELECT r FROM RecommendedMovie r WHERE r.movie.id = ?1")
    RecommendedMovie findByMovieId(int movieId);

    @Query("SELECT r FROM RecommendedMovie r WHERE r.position = ?1")
    RecommendedMovie findByPosition(int position);

    @Query("SELECT CASE WHEN EXISTS (SELECT r FROM RecommendedMovie r WHERE r.movie.id = ?1) THEN true ELSE false END")
    boolean existsByMovieId(int movieId);

    @Modifying
    @Query("DELETE FROM RecommendedMovie r WHERE r.movie.id = :movieId")
    void deleteByMovieId(@Param("movieId") int movieId);

}
