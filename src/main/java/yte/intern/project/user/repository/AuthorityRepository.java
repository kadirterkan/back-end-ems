package yte.intern.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.user.entities.Authority;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {

}
