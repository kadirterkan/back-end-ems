package yte.intern.project.event.service;

import com.google.zxing.common.BitMatrix;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.configuration.JavaMailConfiguration;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.services.QRCodeService;
import yte.intern.project.event.controller.request.*;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.repository.EventRepository;
import yte.intern.project.user.entities.SimpleUser;
import yte.intern.project.user.configuration.AuthenticationFacade;
import yte.intern.project.user.service.UserService;

import javax.naming.NameNotFoundException;

import java.util.List;
import java.util.Objects;
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
    private final JavaMailConfiguration javaMailConfiguration;

    private static final String SOME_ERROR_OCCURRED = "SOME ERROR OCCURRED";
    private static final String USER_DIDNT_JOIN= "YOU DIDN'T JOINED THIS EVENT";
    private static final String LEFT_SUCCESSFULLY = "YOU LEFT EVENT %s SUCCESSFULLY";
    private static final String EVENT_IS_ALREADY_JOINED = "YOU ALREADY JOINED THIS EVENT";
    private static final String EVENT_IS_FULL = "EVENT WITH NAME %s IS FULL";
    private static final String EVENT_SUCCESSFULLY_DELETED="EVENT WITH NAME %s HAS BEEN SUCCESFULLY DELETED";
    private static final String START_CANT_BE_AFTER_END = "START TIME CANNOT BE AFTER END TIME";
    private static final String EVENT_SUCCESSFULLY_UPDATED = "EVENT HAS BEEN SUCCESSFULLY UPDATED";
    private static final String EVENT_ADDED_SUCCESSFULLY = "EVENT WITH NAME %s ADDED SUCCESSFULLY TO USER %s";
    private static final String EVENT_NOT_FOUND = "EVENT COULDN'T FIND";
    private static final String EVENT_DOESNT_EXIST = "EVENT WITH NAME %s DOESNT EXIST";
    private static final String USER_DOESNT_EXIST = "USER WITH NAME %s DOESNT EXIST";
    private static final String EVENT_SUCCESSFULLY_ADDED = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE DATABASE";
    private static final String EVENT_ALREADY_EXISTS = "EVENT WITH THE NAME %s DOES ALREADY EXIST. PLEASE CHOOSE ANOTHER NAME FOR NOT ANY CONFUSION";


    public CustomEventService(EventRepository eventRepository, UserService userService, AuthenticationFacade authenticationFacade, JavaMailConfiguration javaMailConfiguration) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.javaMailConfiguration = javaMailConfiguration;
    }

    public CustomEvent getEventbyeventName(String eventName) throws NameNotFoundException {
        return eventRepository.findByEventName(eventName)
                .orElseThrow(()->new NameNotFoundException("BELİRTİLEN ETKİNLİK BULUNAMADI"));
    }

    public CustomEvent getEventbyId(Long id) throws Exception {
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
                CustomEvent customEvent = addEventRequest.toEvent(userService.bringUser(username));
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
    public MessageResponse editEvent(EventRequest updateEventRequest){
        String username = authenticationFacade.getAuthentication().getName();
        Optional<CustomEvent> oldEvent = eventRepository.findByEventName(username);
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
        SimpleUser creator = userService.bringUser(username);
        return eventRepository.findAllByCreatedBy(creator);
    }

    public List<CustomEvent> getAllEvents(){
        return eventRepository.findAll();
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
    public BitMatrix getQRCodeBitMatrix(EventRequest request) throws Exception {
        String username = authenticationFacade.getAuthentication().getName();
        SimpleUser simpleUser = userService.bringUser(username);
        String context = simpleUser.toString() + '\n' + request.toString();
        return QRCodeService.generateQRCodeBitMatrix(context);
    }


    @Transactional
    public MessageResponse addUserToEvent(String eventName){
        String userName = authenticationFacade.getAuthentication().getName();
        SimpleUser simpleUser = userService.bringUser(userName);
        if(simpleUser !=null){
            Optional<CustomEvent> customEvent = loadByEventName(eventName);
            if(customEvent.isPresent()){
                if(customEvent.get().isNotFull()){
                    if(userService.joinedThisEvent(simpleUser, customEvent.get())) {
                        simpleUser.AddEventToUser(customEvent.get());
                        customEvent.get().addUserToEvent(simpleUser);

                        userService.updateUser(simpleUser);
                        eventRepository.save(customEvent.get());

                        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

                        mailSender.setHost(this.javaMailConfiguration.getHost());
                        mailSender.setPort(this.javaMailConfiguration.getPort());
                        mailSender.setUsername(this.javaMailConfiguration.getUsername());
                        mailSender.setPassword(this.javaMailConfiguration.getPassword());

                        SimpleMailMessage mailMessage = new SimpleMailMessage();
                        mailMessage.setFrom(Objects.requireNonNull(mailSender.getHost()));
                        mailMessage.setTo(simpleUser.getEmail());
                        mailMessage.setSubject("YOUR QRCODE FOR JOINING THE ");

                        return new MessageResponse(SUCCESS,
                                EVENT_ADDED_SUCCESSFULLY.formatted(eventName,
                                        simpleUser.getUsername()));
                    }
                    else{
                        return new MessageResponse(ERROR,
                                EVENT_IS_ALREADY_JOINED);
                    }
                }
                else{
                    return new MessageResponse(ERROR,EVENT_IS_FULL.formatted(eventName));
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

    public MessageResponse leaveTheEvent(EventRequest eventRequest){
        Optional<CustomEvent> event = eventRepository.findByEventName(eventRequest.getEventName());
        if(event.isPresent()){
            String username = authenticationFacade.getAuthentication().getName();
            SimpleUser simpleUser = userService.bringUser(username);
            if(simpleUser.leaveEvent(event.get())){
                if(event.get().deleteUserFromEvent(simpleUser)){
                    userService.updateUser(simpleUser);
                    eventRepository.save(event.get());
                    return new MessageResponse(SUCCESS,
                            LEFT_SUCCESSFULLY.formatted(eventRequest.getEventName()));
                }
                return new MessageResponse(ERROR,SOME_ERROR_OCCURRED);
            }
            else {
                return new MessageResponse(ERROR,
                        USER_DIDNT_JOIN);
            }
        }else{
            return new MessageResponse(ERROR,
                    EVENT_DOESNT_EXIST.formatted(eventRequest.getEventName()));
        }
    }

}
