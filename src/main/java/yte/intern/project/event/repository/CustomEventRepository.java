package yte.intern.project.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.entities.UserEvent;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.enums.Departments;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CustomEventRepository extends JpaRepository<CustomEvent, Long> {

    List<CustomEvent> findAllByCreatedBy (CustomMod creator);

    Page<CustomEvent> findAllByCreatedBy (CustomMod creator, Pageable pageable);

    Page<CustomEvent> findAllByEventDetailAndCreatedBy(String eventDetail , CustomMod customMod, Pageable pageable);

    Page<CustomEvent> findAllByEventDetailIsNotAndCreatedBy(String eventDetail , CustomMod customMod, Pageable pageable);

    Page<CustomEvent> findAllByEventPrivacyEqualsOrEventPrivacyEquals(Departments company, Departments privateDepartment,Pageable pageable);

    Page<CustomEvent> findAllByEventDetailAndEventPrivacyEqualsOrEventPrivacyEquals(String eventDetail, Departments company, Departments privateDepartment,Pageable pageable);

    Page<CustomEvent> findAllByEventDetailIsNotAndEventPrivacyEqualsOrEventPrivacyEquals(String eventDetail, Departments company, Departments privateDepartment,Pageable pageable);

    Page<CustomEvent> findAllByUsersIn(Collection<Set<UserEvent>> users, Pageable pageable);

    List<CustomEvent> findAll();

    List<CustomEvent> findAllByStartTimeBefore(LocalDateTime localDateTime);

    List<CustomEvent> findAllByCreatedByAndStartTimeBefore(CustomMod customMod,LocalDateTime localDateTime);

    List<CustomEvent> findAllByCreatedByAndStartTimeAfter(CustomMod customMod,LocalDateTime localDateTime);
}