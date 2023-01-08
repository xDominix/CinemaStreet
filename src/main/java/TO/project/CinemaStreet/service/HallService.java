package TO.project.CinemaStreet.service;
import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class HallService {
    private final HallRepository hallRepository;
    private final HallMovieRepository hallMovieRepository;

    public HallService(HallRepository hallRepository,HallMovieRepository hallMovieRepository) {
        this.hallRepository = hallRepository;
        this.hallMovieRepository = hallMovieRepository;
    }

    public List<Hall> getAllHalls() {
        return hallRepository.findAll();
    }

    public Hall getHallById(int id){
        Optional<Hall> hall = hallRepository.findById(id);
        return hall.orElse(null);
    }
    public void addHall(Hall hall) {
        hallRepository.save(hall);
    }


    public boolean deleteHallById(Integer id) {
        if (hallRepository.existsById(id)) {
            hallRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void removeAllHalls() {
        hallMovieRepository.deleteAll();
        hallRepository.deleteAll();
        hallRepository.flush();
    }

    public List<Hall> getHallsFromJson(String jsonPath) {
        String json = "";
        try {
            json = new String(java.nio.file.Files.readAllBytes(Path.of(jsonPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        read json string and deseralize
        ObjectMapper mapper = new ObjectMapper();
        List<Hall> halls = null;
        try {
            halls = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Hall.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return halls;
    }

    public void addHalls(List<Hall> newHalls) {
        for (Hall hall : newHalls) {
            addHall(hall);
        }
    }

}
