package yte.intern.project.event.service;

import com.google.zxing.common.BitMatrix;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.embeddable.UserEventId;
import yte.intern.project.common.entities.UserEvent;
import yte.intern.project.common.services.JavaMailService;
import yte.intern.project.common.services.UserEventService;
import yte.intern.project.common.utils.QRcodeUtil;
import yte.intern.project.event.controller.request.*;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.repository.EventRepository;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.configuration.AuthenticationFacade;
import yte.intern.project.user.service.UserService;

import javax.naming.NameNotFoundException;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
public class CustomEventService {

    @Value("SOMESECRETCODEABOUTTHISJAVASPRINGBOOTAPPLICATIONYOUMAYNOTSOLVETHISFORGODSSAKE")
    private String SecretKey;

    private final EventRepository eventRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final UserEventService userEventService;
    private final JavaMailService javaMailService;

    private static final String SOME_ERROR_OCCURRED = "SOME ERROR OCCURRED";
    private static final String USER_DIDNT_JOIN= "YOU DIDN'T JOINED THIS EVENT";
    private static final String LEFT_SUCCESSFULLY = "YOU LEFT EVENT SUCCESSFULLY";
    private static final String EVENT_IS_ALREADY_JOINED = "YOU ALREADY JOINED THIS EVENT";
    private static final String EVENT_IS_FULL = "EVENT WITH NAME %s IS FULL";
    private static final String EVENT_SUCCESSFULLY_DELETED="EVENT WITH NAME %s HAS BEEN SUCCESFULLY DELETED";
    private static final String START_CANT_BE_AFTER_END = "START TIME CANNOT BE AFTER END TIME";
    private static final String EVENT_SUCCESSFULLY_UPDATED = "EVENT HAS BEEN SUCCESSFULLY UPDATED";
    private static final String EVENT_ADDED_SUCCESSFULLY = "EVENT WITH NAME %s ADDED SUCCESSFULLY TO USER %s";
    private static final String EVENT_NOT_FOUND = "EVENT COULDN'T FIND";
    private static final String EVENT_DOESNT_EXIST = "EVENT DOESNT EXIST";
    private static final String USER_DOESNT_EXIST = "USER WITH NAME %s DOESNT EXIST";
    private static final String EVENT_SUCCESSFULLY_ADDED = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE DATABASE";
    private static final String EVENT_ALREADY_EXISTS = "EVENT WITH THE NAME %s DOES ALREADY EXIST. PLEASE CHOOSE ANOTHER NAME FOR NOT ANY CONFUSION";


    public CustomEventService(EventRepository eventRepository, UserService userService, AuthenticationFacade authenticationFacade, UserEventService userEventService, JavaMailService javaMailService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.userEventService = userEventService;
        this.javaMailService = javaMailService;
    }

    public CustomEvent getEventbyeventName(String eventName) throws NameNotFoundException {
        return eventRepository.findByEventName(eventName)
                .orElseThrow(()->new NameNotFoundException("BELİRTİLEN ETKİNLİK BULUNAMADI"));
    }

    public CustomEvent getEventById(Long id) throws Exception {
        return eventRepository.findById(id)
                .orElseThrow(()-> new Exception("ETKİNLİK BULUNAMADI"));
    }

    @Transactional
    public MessageResponse addEventToDb(EventRequest addEventRequest){
        String username = authenticationFacade.getAuthentication().getName();
        if(!eventRepository.existsByEventName(addEventRequest.getEventName())){
            if(addEventRequest.getStartTime().isAfter(addEventRequest.getEndTime())){
                return new MessageResponse(ERROR,
                        START_CANT_BE_AFTER_END);
            }
            else{
                CustomMod creator = userService.bringMod(username);
                CustomEvent customEvent = addEventRequest.toEvent(creator);
                creator.addCreatedEvent(customEvent);
                userService.updateMod(creator);

                eventRepository.save(customEvent);

                return new MessageResponse(SUCCESS,
                        EVENT_SUCCESSFULLY_ADDED.formatted(addEventRequest.getEventName()));
            }
        }
        else {
            return new MessageResponse(ERROR,
                    EVENT_ALREADY_EXISTS.formatted(addEventRequest.getEventName()));
        }
    }

    @Transactional
    public Optional<CustomEvent> loadByEventName(String eventName){
        return eventRepository.findByEventName(eventName);
    }

    @Transactional
    public MessageResponse editEvent(UpdateEventRequest updateEventRequest){
        String username = authenticationFacade.getAuthentication().getName();
        Optional<CustomEvent> oldEvent = eventRepository.findByEventName(updateEventRequest.getOldEventName());
        if(oldEvent.isPresent()){
            oldEvent.get().updateCustomEvent(updateEventRequest);
            eventRepository.save(oldEvent.get());
            return new MessageResponse(SUCCESS,EVENT_SUCCESSFULLY_UPDATED);
        }
        else{
            return new MessageResponse(ERROR,EVENT_NOT_FOUND);
        }
    }

    @Transactional
    public List<CustomEvent> getUsersEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser creator = userService.bringUser(username);
        return eventRepository.findAllByCreatedBy(creator);
    }

    public List<CustomEvent> getAllEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser user = userService.bringUser(username);
        return eventRepository
                .findAll()
                .stream()
                .filter(event -> event.getUsers()
                        .stream()
                        .noneMatch(userEvent -> userEvent.getUser()
                                .equals(user)))
                .toList();
    }

    public boolean eventCreatedByThisUser(CustomEvent customEvent,String username){
        return customEvent.getCreatedBy().getUsername().equals(username);
    }

    @Transactional
    public MessageResponse deleteEvent(EventRequest deleteEventRequest){
        String eventName = deleteEventRequest.getEventName();
        var delEvent = eventRepository.findByEventName(eventName);
        if(delEvent.isPresent()){
            eventRepository.delete(delEvent.get());
            return new MessageResponse(SUCCESS,EVENT_SUCCESSFULLY_DELETED.formatted(eventName));
        }
        else{
            return new MessageResponse(ERROR,EVENT_DOESNT_EXIST.formatted(eventName));
        }
    }


    @Transactional
    public String getQRCodeBase64(LittleEventRequest littleEventRequest) throws Exception {
        CustomEvent customEvent = getEventbyeventName(littleEventRequest.getEventName());
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);
        UserEvent userEvent = userEventService.getUserEvent(customUser,customEvent);

        String context = customUser.toString() + '\n' + customEvent.toString() + '\n' + userEvent.toString();
        return QRcodeUtil.generateQRCodeBase64(context);
    }


    @Transactional
    public MessageResponse addUserToEvent(String eventName) throws Exception {
        String userName = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(userName);
        if(customUser !=null){
            CustomEvent customEvent = getEventbyeventName(eventName);
            if(customEvent!=null){
                if(customEvent.isNotFull()){
                    if(!userService.joinedThisEvent(customUser, customEvent)) {
                        UserEventId userEventId = new UserEventId(customUser.getId(),customEvent.getId());
                        UserEvent userEvent = new UserEvent(userEventId,customUser,customEvent, LocalDateTime.now());
                        userEventService.addUserEventToDb(userEvent);

                        customUser.AddEventToUser(userEvent);
                        customEvent.addUserToEvent(userEvent);

                        userService.updateUser(customUser);

                        javaMailService.mailSender(customUser,customEvent,userEvent.getJoinTime());

                        return new MessageResponse(SUCCESS,
                                EVENT_ADDED_SUCCESSFULLY.formatted(customEvent.getEventName(),
                                        customUser.getUsername()));
                    }
                    else{
                        return new MessageResponse(ERROR,
                                EVENT_IS_ALREADY_JOINED);
                    }
                }
                else{
                    return new MessageResponse(ERROR,EVENT_IS_FULL.formatted(customEvent.getEventName()));
                }

            }
            else {
                return new MessageResponse(ERROR,
                        EVENT_DOESNT_EXIST.formatted(eventName));
            }
        }
        else{
            return new MessageResponse(ERROR,
                    USER_DOESNT_EXIST.formatted(userName));
        }
    }

    public MessageResponse leaveTheEvent(Long eventId) throws Exception {
        Optional<CustomEvent> event = eventRepository.findById(eventId);
        if(event.isPresent()){
            String username = authenticationFacade.getAuthentication().getName();
            CustomUser customUser = userService.bringUser(username);
            UserEvent userEvent = userEventService.getUserEvent(customUser,event.get());
            if (userEvent!=null) {
                if(customUser.leaveEvent(userEvent)){
                    if(event.get().deleteUserFromEvent(userEvent)){
                        userEventService.deleteUserEvent(customUser,event.get());
                        userService.updateUser(customUser);
                        eventRepository.save(event.get());
                        return new MessageResponse(SUCCESS,
                                LEFT_SUCCESSFULLY);
                    }
                    return new MessageResponse(ERROR,SOME_ERROR_OCCURRED);
                }
                else {
                    return new MessageResponse(ERROR,
                            USER_DIDNT_JOIN);
                }
            } else {
                return new MessageResponse(ERROR,
                        USER_DIDNT_JOIN);
            }
        }else{
            return new MessageResponse(ERROR,
                    EVENT_DOESNT_EXIST);
        }
    }

    public List<CustomEvent> getJoinedEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);
        return userEventService.getAllJoinedEvents(customUser)
                .stream()
                .map(UserEvent::getEvent)
                .toList();
    }

    public List<CustomUser> getEventJoinedUsers(Long eventId) throws Exception {
        CustomEvent customEvent = getEventById(eventId);
        return customEvent.getUsers()
                .stream()
                .map(UserEvent::getUser).toList();
    }
}
