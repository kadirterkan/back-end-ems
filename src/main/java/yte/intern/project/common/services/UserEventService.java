package yte.intern.project.common.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.entities.UserEvent;
import yte.intern.project.common.repository.UserEventRepository;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.user.entities.CustomUser;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(()-> new Exception("ETKİNLİK BULUNAMADI"));
    }

    @Transactional
    public void deleteUserEvent(CustomUser customUser, CustomEvent customEvent){
        userEventRepository.deleteUserEventByUserAndEvent(customUser, customEvent);
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
    public List<UserEvent> getAllJoinedEvents(CustomUser customUser){
        return userEventRepository.findAllByUser(customUser);
    }
}
