package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.HallMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HallMovieRepository extends JpaRepository<HallMovie, Integer>{

    @Query ("SELECT h FROM HallMovie h WHERE h.movieId = ?1")
    List<HallMovie> findAllByMovieId(int movieId);

//    @Query ("SELECT h FROM HallMovie h WHERE h.hallId = ?1")
    List<HallMovie> findAllByHallId(int hallId);

    @Query ("SELECT h FROM HallMovie h WHERE h.hallId = ?1 AND h.movieId = ?2 AND h.date = ?3")
    HallMovie findByHallIdAndMovieIdAndDate(int hallId, int movieId, LocalDateTime date);

    List<HallMovie> findAllByHallIdAndMovieId(int hallId, int movieId);

}