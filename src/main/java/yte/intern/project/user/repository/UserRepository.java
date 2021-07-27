package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.AppUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,Long> {
    boolean existsById(Long id);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByTcKimlikNumber(String tcKimlikNumber);

    boolean existsByUsername(String username);

}
