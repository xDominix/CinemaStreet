package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.Roles;
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
                User admin = new User("admin", "123qwe", "Admin", "Adminowy", "admin@citystreet.com", Roles.ADMIN);
                userRepository.save(admin);
            }
            else if (userRepository.count() >= 1) {
                userRepository.deleteAll();
                this.commandLineRunner(userRepository);
            }
        };
    }
}
