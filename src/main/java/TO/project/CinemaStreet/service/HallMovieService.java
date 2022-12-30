package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.HallMovie;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HallMovieService
{
    private final HallMovieRepository hallMovieRepository;
    public HallMovieService(HallMovieRepository hallMovieRepository) {
        this.hallMovieRepository = hallMovieRepository;
    }
    public List<HallMovie> getAllHallMovies()
    {
   return hallMovieRepository.findAll();
       //return FXCollections.observableArrayList(new HallMovie());
    }
    public void addHallMovie(HallMovie hallMovie)
    {
        hallMovieRepository.save(hallMovie);
    }

    public boolean deleteHallMovieById(Integer id)
    {
        if (hallMovieRepository.existsById(id)) {
            hallMovieRepository.deleteById(id);
            return true;
        }

        return false;

    }
}
