package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.user.entities.SimpleUser;

import java.util.Optional;

public interface SimpleUserRepository extends JpaRepository<SimpleUser,Long> {
    boolean existsById(Long id);

    Optional<SimpleUser> findById(Long id);

    Optional<SimpleUser> findByUsername(String username);

    Optional<SimpleUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByTcKimlikNumber(String tcKimlikNumber);

    boolean existsByUsername(String username);

    boolean existsAppUserByCustomEventSetContains(CustomEvent customEvent);
}
