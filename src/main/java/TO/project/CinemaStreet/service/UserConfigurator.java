package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.Role;
import TO.project.CinemaStreet.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfigurator {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User kowalski = new User("Jan", "Kowalski", "temp@gmail.com", Role.USER);
                userRepository.save(kowalski);
            }
//            else if (userRepository.count() == 1) {
//                userRepository.deleteAll();
//            }
        };
    }
}
