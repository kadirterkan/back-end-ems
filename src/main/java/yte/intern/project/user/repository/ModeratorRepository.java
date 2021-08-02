package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.Moderator;

public interface ModeratorRepository extends JpaRepository<Moderator,Long> {

}
