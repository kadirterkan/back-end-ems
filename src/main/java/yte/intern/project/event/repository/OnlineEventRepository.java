package yte.intern.project.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.event.entities.OnlineEvent;

public interface OnlineEventRepository extends JpaRepository<OnlineEvent, Long> {
}