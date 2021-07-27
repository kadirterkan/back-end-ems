package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    boolean existsByName(String name);

    @Override
    Optional<Authority> findById(Long aLong);

    Optional<Authority> findByName(String name);

}
