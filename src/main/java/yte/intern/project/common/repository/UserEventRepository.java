package yte.intern.project.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yte.intern.project.common.embeddable.UserEventId;
import yte.intern.project.common.entities.UserEvent;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomUser;

import java.util.List;
import java.util.Optional;

public interface UserEventRepository extends JpaRepository<UserEvent, UserEventId> {

    List<UserEvent> findByUser(CustomUser user);

    List<UserEvent> findByEvent(CustomEvent event);

    List<UserEvent> findAllByUser(CustomUser user);

    Optional<UserEvent> findByUserAndEvent(CustomUser customUser,CustomEvent customEvent);

    void deleteUserEventByUserAndEvent(CustomUser customUser,CustomEvent customEvent);

    boolean existsByUserAndEvent(CustomUser customUser,CustomEvent customEvent);
}
