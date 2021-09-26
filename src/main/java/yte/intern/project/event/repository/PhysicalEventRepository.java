package yte.intern.project.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.PhysicalEvent;

public interface PhysicalEventRepository extends JpaRepository<PhysicalEvent, Long> {

    @Override
    boolean existsById(Long aLong);

    PhysicalEvent getPhysicalEventById(Long id);
}