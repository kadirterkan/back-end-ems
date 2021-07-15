package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.User;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsById(Long id);
}
