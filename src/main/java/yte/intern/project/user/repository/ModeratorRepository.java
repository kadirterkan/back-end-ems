package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.CustomMod;

import java.util.Optional;

public interface ModeratorRepository extends JpaRepository<CustomMod,Long> {

    boolean existsByUsername(String username);

    Optional<CustomMod> findByUsername(String username);
}
