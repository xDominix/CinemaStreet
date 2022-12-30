package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Roles;
import TO.project.CinemaStreet.model.Movie;
import TO.project.CinemaStreet.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class UserConfigurator {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository,MovieRepository movieRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = new User("admin", "123qwe", "Admin", "Adminowy", "admin@citystreet.com", Roles.ADMIN);
                userRepository.save(admin);
            }
            else if (userRepository.count() >= 1) {
                userRepository.deleteAll();
                this.commandLineRunner(userRepository, movieRepository);
            }

            if (movieRepository.count() == 0) {
                LocalDateTime avatarDate = LocalDateTime.of(2009, 12, 10, 0, 0);
                Movie movie = new Movie("Avatar", 135, avatarDate, 25.0f);
                movieRepository.save(movie);
            }
        };
    }
}
