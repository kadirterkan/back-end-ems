package yte.intern.project.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event,Long> {

    @Override
    boolean existsById(Long aLong);

    @Override
    Optional<Event> findById(Long aLong);

    Optional<Event> findByEventName(String eventname);
}
