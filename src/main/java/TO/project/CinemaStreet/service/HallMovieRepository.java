package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.HallMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallMovieRepository extends JpaRepository<HallMovie, Integer>
{

}