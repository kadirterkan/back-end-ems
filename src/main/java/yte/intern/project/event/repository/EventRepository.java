package yte.intern.project.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.CustomEvent;

import java.util.Optional;

public interface EventRepository extends JpaRepository<CustomEvent,Long> {

    @Override
    boolean existsById(Long aLong);

    boolean existsByEventName(String name);

    @Override
    Optional<CustomEvent> findById(Long aLong);

    Optional<CustomEvent> findByEventName(String eventName);
}
