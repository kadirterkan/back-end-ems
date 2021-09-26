package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import yte.intern.project.user.entities.BaseUser;

public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {

    UserDetails findByUsername(String username);

    BaseUser getByUsername(String username);

    BaseUser findByEmail(String email);

    boolean existsByEmail(String email);
}