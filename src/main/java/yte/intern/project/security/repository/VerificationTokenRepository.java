package yte.intern.project.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.BaseUser;
import yte.intern.project.security.entities.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Override
    Optional<VerificationToken> findById(Long aLong);

    VerificationToken findByToken(String token);

    VerificationToken findByUser(BaseUser user);
}