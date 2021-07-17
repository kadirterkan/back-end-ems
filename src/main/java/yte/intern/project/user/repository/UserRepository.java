package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsById(Long id);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);
}
