package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.Hall;
import TO.project.CinemaStreet.model.HallMovie;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Configuration
public class UserConfigurator {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,MovieRepository movieRepository, HallMovieRepository hallMovieRepository, HallRepository hallRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User("admin", "123qwe", "Admin", "Adminowy", "admin@citystreet.com", Roles.ADMIN);
                System.out.println("dodaje admina");
                userRepository.save(admin);
            }
//            else if (userRepository.count() >= 1) {
//                userRepository.deleteAll();
//                this.commandLineRunner(userRepository, movieRepository);
//            }
//
//            if (movieRepository.count() == 0) {
////                LocalDateTime avatarDate = LocalDateTime.of(2009, 12, 10, 0, 0);
////                Movie movie = new Movie("Avatar", 135, avatarDate, 25.0f);
////                movieRepository.save(movie);
//
//                LocalDateTime avatarDate = LocalDateTime.of(2009, 12, 10, 0, 0);
//                LocalDateTime godfatherDate = LocalDateTime.of(1972, 3, 15, 0, 0);
//
//                Movie movie = new Movie("Avatar", 135, avatarDate, 25.0f);
//                Movie movie2 = new Movie("Godfather", 90, godfatherDate, 10.0f);
//
//                movieRepository.save(movie);
//                movieRepository.save(movie2);
//            }
//            if(hallRepository.count() == 0){
//                hallRepository.save(new Hall(1, 20));
//                hallRepository.save(new Hall(2, 50));
//            }
//            if(hallMovieRepository.count() == 0)
//            {
//                Movie movie2 = movieRepository.findAll().get(0);
//                HallMovie godfatherHallMovie = new HallMovie(hallRepository.findAll().get(0),movie2, LocalDateTime.now().plus(2, ChronoUnit.DAYS));
//                hallMovieRepository.save(godfatherHallMovie);
//
//                HallMovie avatarHallMovie = new HallMovie(hallRepository.findAll().get(1),movie2, LocalDateTime.now().plus(3, ChronoUnit.DAYS).plus(12, ChronoUnit.HOURS));
//                hallMovieRepository.save(avatarHallMovie);
//            }
        };
    }
}
