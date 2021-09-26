package yte.intern.project.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yte.intern.project.event.embeddable.UserEventId;
import yte.intern.project.event.entities.UserEvent;
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

    @Query(
            value = "SELECT u FROM UserEvent u where u.user = :user and u.event.eventDetail = :eventDetails",
            countQuery = "SELECT count(*) FROM UserEvent",
            nativeQuery = true
    )
    Page<UserEvent> findAllUserEventsByCustomUserAndEventDetailsNamedParamsWithPagination(@Param("user") CustomUser customUser,@Param("eventDetails") String eventDetails,Pageable pageable);

    @Query(
            value = "SELECT u FROM UserEvent u where u.user = :user and u.event.eventDetail != :eventDetails",
            countQuery = "SELECT count(*) FROM UserEvent",
            nativeQuery = true
    )
    Page<UserEvent> findAllUserEventsByCustomUserAndEventDetailsIsNotNamedParamsWithPagination(@Param("user") CustomUser customUser,@Param("eventDetails") String eventDetails,Pageable pageable);

    void deleteByUserAndEvent(CustomUser customUser,CustomEvent customEvent);

    void delete(UserEvent userEvent);
}
