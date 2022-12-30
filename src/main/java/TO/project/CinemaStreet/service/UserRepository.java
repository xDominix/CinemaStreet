package TO.project.CinemaStreet.service;

import TO.project.CinemaStreet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsernameAndPassword(String username, String password);
    User getReferenceByUsernameAndPassword(String username, String password);
}
