package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.UserEvent;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomUser;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser,Long> {
    boolean existsById(Long id);

    Optional<CustomUser> findById(Long id);

    Optional<CustomUser> findByUsername(String username);

    Optional<CustomUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByTcKimlikNumber(String tcKimlikNumber);

    boolean existsByUsername(String username);

    boolean existsAppUserByEventsContains(UserEvent userEvent);

//    boolean existsByUsernameAndEventsContains(String tcKimlikNumber,CustomEvent customEvent);
}
