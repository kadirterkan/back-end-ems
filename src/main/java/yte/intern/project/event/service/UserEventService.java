package yte.intern.project.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.event.entities.UserEvent;
import yte.intern.project.event.repository.UserEventRepository;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomUser;

import java.util.List;

@Service
public class UserEventService {

    private final UserEventRepository userEventRepository;

    @Autowired
    public UserEventService(UserEventRepository userEventRepository) {
        this.userEventRepository = userEventRepository;
    }

    @Transactional
    public UserEvent getUserEvent(CustomUser customUser, CustomEvent customEvent) throws Exception {
        return userEventRepository.findByUserAndEvent(customUser,customEvent)
                .orElseThrow(()-> new Exception("EVENT NOT FOUND"));
    }

    @Transactional
    public void deleteUserEvent(CustomUser customUser, CustomEvent customEvent){
        userEventRepository.deleteUserEventByUserAndEvent(customUser, customEvent);
    }

    @Transactional
    void deleteUserEventWithUserEvent(UserEvent userEvent){
        userEventRepository.delete(userEvent);
    }

    @Transactional
    public void addUserEventToDb(UserEvent userEvent){
        userEventRepository.save(userEvent);
    }

    @Transactional
    public boolean doesUserEventExists(CustomUser customUser, CustomEvent customEvent){
        return userEventRepository.existsByUserAndEvent(customUser, customEvent);
    }

    @Transactional
    public List<UserEvent> getAllUserEventsForUser(CustomUser customUser){
        return userEventRepository.findAllByUser(customUser);
    }

    public List<UserEvent> getAllUserEventsByEvent(CustomEvent customEvent) {
        return userEventRepository.findByEvent(customEvent);
    }

    public List<UserEvent> findAllUserEventsByCustomUserAndEventDetailsNamedParamsWithPagination(CustomUser customUser, String eventDetails, Pageable pageable){
        return userEventRepository.findAllUserEventsByCustomUserAndEventDetailsNamedParamsWithPagination(customUser, eventDetails, pageable).stream().toList();
    }

    public List<UserEvent> findAllUserEventsByCustomUserAndEventDetailsIsNotNamedParamsWithPagination(CustomUser customUser, String eventDetails, Pageable pageable){
        return userEventRepository.findAllUserEventsByCustomUserAndEventDetailsIsNotNamedParamsWithPagination(customUser,eventDetails,pageable).stream().toList();
    }

    public void deleteByUserAndEvent(CustomUser customUser, CustomEvent customEvent){
        userEventRepository.deleteByUserAndEvent(customUser,customEvent);
    }

    public boolean joinedThisEvent(CustomUser customUser, CustomEvent customEvent) {
        return !userEventRepository.existsByUserAndEvent(customUser, customEvent);
    }
}
