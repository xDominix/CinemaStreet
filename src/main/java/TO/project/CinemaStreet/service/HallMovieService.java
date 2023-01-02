package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HallMovieService
{
    private final HallMovieRepository hallMovieRepository;
    private final HallService hallService;
    public HallMovieService(HallMovieRepository hallMovieRepository,HallService hallService) {
        this.hallMovieRepository = hallMovieRepository;
        this.hallService = hallService;
    }
    public HallMovie getHallMovieById(int id){
        Optional<HallMovie> hallMovie = hallMovieRepository.findById(id);
        return hallMovie.orElse(null);
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

    public List<HallMovie> getHallMoviesByMovie(Movie movie)
    {
        return hallMovieRepository.findAllByMovieId(movie.getId());
    }

    public List<HallMovie> getHallMoviesByHall(Hall hall)
    {
        return hallMovieRepository.findAllByHallId(hall.getId());
    }

    public HallMovie getHallMovieByHallAndMovieAndDate(Hall hall, Movie movie, LocalDateTime date)
    {
        return hallMovieRepository.findByHallIdAndMovieIdAndDate(hall.getId(), movie.getId(), date);
    }

    public List<HallMovie> getHallMoviesByHallAndMovie(Hall hall, Movie movie)
    {
        return hallMovieRepository.findAllByHallIdAndMovieId(hall.getId(), movie.getId());
    }


    public void removeAllHallMovies()
    {
        hallMovieRepository.deleteAll();
        hallMovieRepository.flush();
    }

    public void updateHallMovie(HallMovie hallMovie) {
        hallMovieRepository.save(hallMovie);
    }

    public void validateHallMovies(){
        List<HallMovie> hallMovies = hallMovieRepository.findAll();
        List<HallMovie> hallMoviesToRemove = new ArrayList<>();

//        check if hall exists
        for (HallMovie hallMovie : hallMovies) {
            if (hallService.getHallById(hallMovie.getHallId()) == null) {
                hallMoviesToRemove.add(hallMovie);
            }
        }
        hallMovieRepository.deleteAll(hallMoviesToRemove);
        hallMovieRepository.flush();

        hallMovies = hallMovieRepository.findAll();
        hallMoviesToRemove = new ArrayList<>();
        for (HallMovie hallMovie : hallMovies) {
            if (hallMovie.getDate().isBefore(LocalDateTime.now())) {
                hallMoviesToRemove.add(hallMovie);
            }
        }
        hallMovieRepository.deleteAll(hallMoviesToRemove);
        hallMovieRepository.flush();

        hallMovies = hallMovieRepository.findAll();
        hallMoviesToRemove = new ArrayList<>();

        for (HallMovie hallMovie : hallMovies) {
            if (hallMovie.getSeatsTaken() > hallService.getHallById(hallMovie.getHallId()).getSeatsNumber()) {
                hallMoviesToRemove.add(hallMovie);
                System.out.println("usuń"+hallMovie);
            }
        }
        hallMovieRepository.deleteAll(hallMoviesToRemove);
        hallMovieRepository.flush();
    }

    public void updateAllHallMovies(){
        List<HallMovie> hallMovies = hallMovieRepository.findAll();
        for (HallMovie hallMovie : hallMovies) {
            hallMovieRepository.save(hallMovie);
        }
    }

    public int howManySeatsLeft(Integer id) {
        HallMovie hallMovie = getHallMovieById(id);
        return hallService.getHallById(hallMovie.getHallId()).getSeatsNumber() - hallMovie.getSeatsTaken();
    }

}
