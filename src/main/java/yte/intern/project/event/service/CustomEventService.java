package yte.intern.project.event.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.event.embeddable.UserEventId;
import yte.intern.project.event.entities.*;
import yte.intern.project.common.services.JavaMailService;
import yte.intern.project.common.utils.QRcodeUtil;
import yte.intern.project.event.controller.request.*;
import yte.intern.project.event.enums.EventType;
import yte.intern.project.event.enums.Status;
import yte.intern.project.event.repository.CustomEventRepository;
import yte.intern.project.event.repository.OnlineEventRepository;
import yte.intern.project.event.repository.PhysicalEventRepository;
import yte.intern.project.event.repository.QuestionAnswersRepository;
import yte.intern.project.user.entities.CustomMod;
import yte.intern.project.user.entities.CustomUser;
import yte.intern.project.user.configuration.AuthenticationFacade;
import yte.intern.project.user.enums.Departments;
import yte.intern.project.user.service.UserService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
public class CustomEventService {

    @Value("SOMESECRETCODEABOUTTHISJAVASPRINGBOOTAPPLICATIONYOUMAYNOTSOLVETHISFORGODSSAKE")
    private String SecretKey;

    private final CustomEventRepository customEventRepository;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final UserEventService userEventService;
    private final JavaMailService javaMailService;
    private final PhysicalEventRepository physicalEventRepository;
    private final OnlineEventRepository onlineEventRepository;
    private final QuestionAnswersRepository questionAnswersRepository;

    private static final String SOME_ERROR_OCCURRED = "SOME ERROR OCCURRED";
    private static final String EVENT_CANT_DELETE = "EVENT CANNOT BE DELETED";
    private static final String SUCCESSFULLY_SAVED = "EVENT SUCCESSFULLY SAVED";
    private static final String USER_DIDNT_JOIN = "YOU DIDN'T JOINED THIS EVENT";
    private static final String LEFT_SUCCESSFULLY = "YOU LEFT EVENT SUCCESSFULLY";
    private static final String EVENT_IS_ALREADY_JOINED = "YOU ALREADY JOINED THIS EVENT";
    private static final String EVENT_IS_FULL = "EVENT WITH NAME %s IS FULL";
    private static final String EVENT_SUCCESSFULLY_DELETED="EVENT WITH ID %s HAS BEEN SUCCESFULLY DELETED";
    private static final String START_CANT_BE_AFTER_END = "START TIME CANNOT BE AFTER END TIME";
    private static final String EVENT_SUCCESSFULLY_UPDATED = "EVENT HAS BEEN SUCCESSFULLY UPDATED";
    private static final String EVENT_EVENT_SUCCESSFULLY = "JOINED TO EVENT WITH ID %s SUCCESSFULLY";
    private static final String EVENT_NOT_FOUND = "EVENT COULDN'T FIND";
    private static final String EVENT_DOESNT_EXIST = "EVENT DOESNT EXIST";
    private static final String USER_DOESNT_EXIST = "USER WITH NAME %s DOESNT EXIST";
    private static final String EVENT_SUCCESSFULLY_ADDED = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE DATABASE";
    private static final String EVENT_ALREADY_EXISTS = "EVENT WITH THE NAME %s DOES ALREADY EXIST. PLEASE CHOOSE ANOTHER NAME FOR NOT ANY CONFUSION";


    @Autowired
    public CustomEventService(CustomEventRepository customEventRepository, UserService userService, AuthenticationFacade authenticationFacade, UserEventService userEventService, JavaMailService javaMailService, PhysicalEventRepository physicalEventRepository, OnlineEventRepository onlineEventRepository, QuestionAnswersRepository questionAnswersRepository) {
        this.customEventRepository = customEventRepository;
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.userEventService = userEventService;
        this.javaMailService = javaMailService;
        this.physicalEventRepository = physicalEventRepository;
        this.onlineEventRepository = onlineEventRepository;
        this.questionAnswersRepository = questionAnswersRepository;
    }

    public CustomEvent getEventById(Long id) throws Exception {
        return customEventRepository.findById(id)
                .orElseThrow(()-> new Exception("EVENT NOT FOUND"));
    }

    @Transactional
    public MessageResponse addOnlineEventToDb(OnlineEventRequest onlineEventRequest) throws IOException {
        String username = authenticationFacade.getAuthentication().getName();
            if(onlineEventRequest.getStartTime().isAfter(onlineEventRequest.getEndTime())){
                return new MessageResponse(ERROR,
                        START_CANT_BE_AFTER_END);
            }
            else{
                CustomMod creator = userService.bringMod(username);
                OnlineEvent newOnlineEvent = onlineEventRequest.toOnlineEvent(creator);
                creator.addCreatedEvent(newOnlineEvent);
                userService.updateMod(creator);

                onlineEventRepository.save(newOnlineEvent);

                return new MessageResponse(SUCCESS,
                        EVENT_SUCCESSFULLY_ADDED.formatted(onlineEventRequest.getEventName()));
            }
    }

    @Transactional
    public MessageResponse addPhysicalEventToDb(PhysicalEventRequest physicalEventRequest) throws IOException {
        String username = authenticationFacade.getAuthentication().getName();
        if(physicalEventRequest.getStartTime().isAfter(physicalEventRequest.getEndTime())){
            return new MessageResponse(ERROR,
                    START_CANT_BE_AFTER_END);
        }
        else{
            CustomMod creator = userService.bringMod(username);
            PhysicalEvent newPhysicalEvent = physicalEventRequest.toPhysicalEvent(creator);
            creator.addCreatedEvent(newPhysicalEvent);
            userService.updateMod(creator);
            physicalEventRepository.save(newPhysicalEvent);
            return new MessageResponse(SUCCESS,
                    EVENT_SUCCESSFULLY_ADDED.formatted(physicalEventRequest.getEventName()));
        }
    }


    @Transactional
    public MessageResponse editPhysicalEvent(UpdatePhysicalEvent updatePhysicalEvent) throws IOException {
        Optional<PhysicalEvent> physicalEvent = physicalEventRepository.findById(updatePhysicalEvent.getId());
        if(physicalEvent.isPresent()){
            physicalEvent.get().updateOnlineEvent(updatePhysicalEvent);
            physicalEventRepository.save(physicalEvent.get());
            return new MessageResponse(SUCCESS,EVENT_SUCCESSFULLY_UPDATED);
        }
        else{
            return new MessageResponse(ERROR,EVENT_NOT_FOUND);
        }
    }

    @Transactional
    public MessageResponse editOnlineEvent(UpdateOnlineEvent updateOnlineEvent){
        Optional<OnlineEvent> onlineEvent = onlineEventRepository.findById(updateOnlineEvent.getId());
        if(onlineEvent.isPresent()){
            onlineEvent.get().updateOnlineEvent(updateOnlineEvent);
            onlineEventRepository.save(onlineEvent.get());
            return new MessageResponse(SUCCESS,EVENT_SUCCESSFULLY_UPDATED);
        }
        else{
            return new MessageResponse(ERROR,EVENT_NOT_FOUND);
        }
    }


    public List<CustomEvent> getAllNonJoinedEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser user = userService.bringUser(username);
        return customEventRepository
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
    public MessageResponse deleteEvent(Long eventId){
        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);
            if (onlineEvent.getStartTime().isAfter(LocalDateTime.now())) {
                onlineEvent.getUsers().forEach(this::deleteUserEventFromUserandEvent);

                onlineEventRepository.delete(onlineEvent);
                return new MessageResponse(SUCCESS,EVENT_SUCCESSFULLY_DELETED.formatted(eventId));
            } else {
                return new MessageResponse(ERROR,EVENT_CANT_DELETE);
            }
        } else {
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);
            if (physicalEvent.getStartTime().isAfter(LocalDateTime.now())) {
                physicalEvent.getUsers().forEach(this::deleteUserEventFromUserandEvent);

                physicalEventRepository.delete(physicalEvent);
                return new MessageResponse(SUCCESS,EVENT_SUCCESSFULLY_DELETED.formatted(eventId));
            } else {
                return new MessageResponse(ERROR,EVENT_CANT_DELETE);
            }

        }
    }

    @Transactional
    public void deleteUserEventFromUserandEvent(UserEvent userEvent){
        CustomUser customUser = userEvent.getUser();
        userService.updateUser(customUser);

        if(Objects.equals(userEvent.getEvent().getEventDetail(), "ONLINE")){
            OnlineEvent onlineEvent = onlineEventRepository.getById(userEvent.getEvent().getId());
            onlineEvent.deleteUserFromEvent(userEvent);
            onlineEventRepository.save(onlineEvent);
        }else{
            PhysicalEvent physicalEvent = physicalEventRepository.getById(userEvent.getEvent().getId());
            physicalEvent.deleteUserFromEvent(userEvent);
            physicalEventRepository.save(physicalEvent);
        }

        userEventService.deleteUserEventWithUserEvent(userEvent);
    }


    @Transactional
    public String getQRCodeBase64(EventIdRequest eventIdRequest) throws Exception {
        CustomEvent customEvent = getEventById(eventIdRequest.getEventId());
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);
        UserEvent userEvent = userEventService.getUserEvent(customUser,customEvent);

        String context = customUser.toString() + '\n' + customEvent.toString() + '\n' + userEvent.toString();
        return QRcodeUtil.generateQRCodeBase64(context);
    }

    @Transactional
    public MessageResponse leaveTheEvent(Long eventId) throws Exception {
        CustomEvent customEvent = customEventRepository.getById(eventId);

        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        if (!userEventService.joinedThisEvent(customUser,customEvent)) {
            UserEvent userEvent = userEventService.getUserEvent(customUser,customEvent);

            if(onlineEventRepository.existsById(eventId)){
                OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

                onlineEvent.deleteUserFromEvent(userEvent);

                customUser.leaveEvent(userEvent);

                userEventService.deleteByUserAndEvent(customUser,customEvent);

                onlineEventRepository.save(onlineEvent);

                userService.updateUser(customUser);
                return new MessageResponse(SUCCESS,LEFT_SUCCESSFULLY);
            }else if(physicalEventRepository.existsById(eventId)){
                PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

                physicalEvent.deleteUserFromEvent(userEvent);

                customUser.leaveEvent(userEvent);

                userEventService.deleteByUserAndEvent(customUser,customEvent);

                physicalEventRepository.save(physicalEvent);

                userService.updateUser(customUser);
                return new MessageResponse(SUCCESS,LEFT_SUCCESSFULLY);
            } else{
                return new MessageResponse(ERROR,"THIS EVENT DOESNT EXIST");

            }
        } else {
            return new MessageResponse(ERROR,"YOU HAVE JOINED THIS EVENT");
        }
    }


    @Transactional
    public List<CustomEvent> getAllJoinedEvents(CustomUser customUser){
        return userEventService.getAllUserEventsForUser(customUser)
                .stream()
                .map(UserEvent::getEvent)
                .toList();
    }

    @Transactional
    public List<EventBoxResponse> getGoingEvents(EventsRequest eventsRequest){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);
        Pageable request = PageRequest.of(eventsRequest.getPage(),eventsRequest.getUnits());

        if(eventsRequest.getEventType() == EventType.ALL){

            return userEventService.getAllUserEventsForUser(customUser)
                    .stream()
                    .map(UserEvent::getEvent).map(EventBoxResponse::new)
                    .toList();

        }else if(eventsRequest.getEventType() == EventType.ONLINE){

            return userEventService.findAllUserEventsByCustomUserAndEventDetailsNamedParamsWithPagination(customUser,"online",request)
                    .stream()
                    .map(userEvent -> new EventBoxResponse(userEvent.getEvent()))
                    .toList();
        }else{

            return userEventService.findAllUserEventsByCustomUserAndEventDetailsIsNotNamedParamsWithPagination(customUser,"online",request)
                    .stream()
                    .map(userEvent -> new EventBoxResponse(userEvent.getEvent()))
                    .toList();

        }
    }


    public List<CustomUser> getEventJoinedUsers(Long eventId) throws Exception {
        CustomEvent customEvent = getEventById(eventId);
        return customEvent.getUsers()
                .stream()
                .map(UserEvent::getUser).toList();
    }

    @Transactional
    public List<EventBoxResponse> getCreatedEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomMod creator = userService.bringMod(username);

        return customEventRepository.findAllByCreatedBy(creator)
                .stream().map(EventBoxResponse::new)
                .toList();
    }

    public Date getDateWithoutTimeUsingFormat(LocalDateTime localDateTime) throws ParseException {
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
    }


    public Map<Date,Long> getEventDays(Long id) throws Exception {
        CustomEvent customEvent = getEventById(id);
        Comparator<UserEvent> byJoinDate = new Comparator<UserEvent>() {
            @Override
            public int compare(UserEvent userEvent1, UserEvent userEvent2) {
                return userEvent1.getJoinTime().compareTo(userEvent2.getJoinTime());
            }
        };

        List<UserEvent> userEvent = userEventService.getAllUserEventsByEvent(customEvent);


        return userEvent.stream().collect(Collectors.groupingBy(val -> {
                    try {
                        return getDateWithoutTimeUsingFormat(val.getJoinTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return null;
                },
                Collectors.counting()));
    }


    public List<EventBoxResponse> getAllEvents(EventsRequest eventsRequest){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);
        Pageable request = PageRequest.of(eventsRequest.getPage(),eventsRequest.getUnits());

        if(eventsRequest.getEventType() == EventType.ALL){
            return customEventRepository.findAllByEventPrivacyEqualsOrEventPrivacyEquals(Departments.ALL,customUser.getDepartments(),request)
                    .stream()
                    .map(event -> toEventBoxResponseForUser(event,customUser))
                    .toList();
        }else if(eventsRequest.getEventType() == EventType.ONLINE){
            return customEventRepository.findAllByEventDetailAndEventPrivacyEqualsOrEventPrivacyEquals("ONLINE",Departments.ALL,customUser.getDepartments(),request)
                    .stream()
                    .map(event -> toEventBoxResponseForUser(event,customUser))
                    .toList();
        }else{
            return customEventRepository.findAllByEventDetailIsNotAndEventPrivacyEqualsOrEventPrivacyEquals("ONLINE",Departments.ALL,customUser.getDepartments(),request)
                    .stream()
                    .map(event -> toEventBoxResponseForUser(event,customUser))
                    .toList();
            }
    }

    @Transactional
    public List<EventBoxResponse> getEventsForUser(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        return customEventRepository.findAllByEventPrivacyEqualsOrEventPrivacyEquals(Departments.ALL,customUser.getDepartments(),PageRequest.of(0,5))
                .stream()
                .map(event -> toEventBoxResponseForUser(event,customUser))
                .toList();
    }


    public MessageResponse interestedEventForUser(Long eventId) {
        if (onlineEventRepository.existsById(eventId)) {
            Optional<OnlineEvent> onlineEvent = onlineEventRepository.findById(eventId);

            try{
                String username = authenticationFacade.getAuthentication().getName();
                CustomUser customUser = userService.bringUser(username);

                customUser.addInterestedEvent(onlineEvent.get());
                onlineEvent.get().addInterestedUser(customUser);

                onlineEventRepository.save(onlineEvent.get());
                userService.updateUser(customUser);

                return new MessageResponse(SUCCESS,SUCCESSFULLY_SAVED);
            }catch(Exception e){
                return new MessageResponse(ERROR,e.toString());
            }
        } else {
            PhysicalEvent physicalEvent = physicalEventRepository.getPhysicalEventById(eventId);

            try{
                String username = authenticationFacade.getAuthentication().getName();
                CustomUser customUser = userService.bringUser(username);

                customUser.addInterestedEvent(physicalEvent);
                physicalEvent.addInterestedUser(customUser);

                physicalEventRepository.save(physicalEvent);
                userService.updateUser(customUser);

                return new MessageResponse(SUCCESS,SUCCESSFULLY_SAVED);
            }catch (Exception e){
                return new MessageResponse(ERROR,e.toString());
            }
        }
    }

    private String generateBase64QrCodeForEvent(CustomEvent customEvent,CustomUser joined, LocalDateTime joinTime) throws Exception {
        String barcode = "User with username " + joined.getUsername() + " and TCNO with " + joined.getTcKimlikNumber();
        barcode += "/n/n" + "You joined the event " + customEvent.getEventName() + " which will start at " + customEvent.getStartTime().toString();
        barcode += "/n/n" + "You joined this event at " + joinTime.toString();

        return QRcodeUtil.generateQRCodeBase64(barcode);
    }

    private EventBoxResponse toEventBoxResponseForUser(CustomEvent customEvent,CustomUser customUser){
        List<CustomEvent> joinedEvents = getAllJoinedEvents( customUser);
        Status status = Status.NOTHING;
        if(joinedEvents.contains(customEvent)){
            status = Status.GOING;
        }else if(customEvent.getInterestedUsers().contains(customUser)){
            status = Status.INTERESTED;
        }
        return new EventBoxResponse(customEvent, status);
    }

    public MessageResponse joinOnlineEvent(Long eventId, EventJoinRequest eventJoinRequest) {
        OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        LocalDateTime now = LocalDateTime.now();

        try{
            if (onlineEvent.isNotFull()) {
                if (userEventService.joinedThisEvent(customUser, onlineEvent)) {
                    UserEventId userEventId = new UserEventId(customUser.getId(), eventId);

                    String base64QrCode = generateBase64QrCodeForEvent(onlineEvent,customUser,now);

                    UserEvent userEvent = new UserEvent(userEventId,now, base64QrCode );
                    userEventService.addUserEventToDb(userEvent);

                    customUser.addEventToUser(userEvent);
                    onlineEvent.addUserToEvent(userEvent);

                    userService.updateUser(customUser);
                    onlineEventRepository.save(onlineEvent);

                    javaMailService.mailQrCodeSender(customUser,onlineEvent,base64QrCode);
                } else {
                    throw new Exception(EVENT_IS_ALREADY_JOINED);
                }
            } else {
                throw new Exception(EVENT_IS_FULL);
            }
        }catch (Exception e){
            return new MessageResponse(ERROR,e.toString());
        }finally {
            List<String> answered = eventJoinRequest.getAnswers();
            if (answered != null) {
                QuestionAnswers answers = new QuestionAnswers(answered, onlineEvent, customUser,now);

                questionAnswersRepository.save(answers);
            }
        }
        return new MessageResponse(SUCCESS, EVENT_EVENT_SUCCESSFULLY.formatted(eventId));
    }

    public MessageResponse joinPhysicalEvent(Long eventId, EventJoinRequest eventJoinRequest) {
        PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        LocalDateTime now = LocalDateTime.now();

        try{

            if (physicalEvent.isNotFull()) {
                if (userEventService.joinedThisEvent(customUser, physicalEvent)) {
                    UserEventId userEventId = new UserEventId(customUser.getId(), eventId);

                    String base64QrCode = generateBase64QrCodeForEvent(physicalEvent,customUser,now);

                    UserEvent userEvent = new UserEvent(userEventId,now, base64QrCode );
                    userEventService.addUserEventToDb(userEvent);

                    customUser.addEventToUser(userEvent);
                    physicalEvent.addUserToEvent(userEvent);

                    userService.updateUser(customUser);
                    physicalEventRepository.save(physicalEvent);

                    javaMailService.mailQrCodeSender(customUser,physicalEvent,base64QrCode);
                } else {
                    throw new Exception(EVENT_IS_ALREADY_JOINED);
                }
            } else {
                throw new Exception(EVENT_IS_FULL);
            }
        }catch (Exception e){
            return new MessageResponse(ERROR,e.toString());
        }finally {
            List<String> answered = eventJoinRequest.getAnswers();
            if (answered != null) {
                QuestionAnswers answers = new QuestionAnswers(answered, physicalEvent, customUser,now);

                questionAnswersRepository.save(answers);
            }
        }
        return new MessageResponse(SUCCESS, EVENT_EVENT_SUCCESSFULLY.formatted(eventId));
    }

    @Transactional
    public MessageResponse joinEventWithAnswers(Long eventId,List<String> answers) throws Exception {
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        LocalDateTime now = LocalDateTime.now();

        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

            if(onlineEvent.isNotFull()){
                if(onlineEvent.getStartTime().isAfter(now)){
                    if(userEventService.joinedThisEvent(customUser,onlineEvent)){
                        UserEventId userEventId = new UserEventId(customUser.getId(), eventId);

                        String base64QrCode = generateBase64QrCodeForEvent(onlineEvent,customUser,now);

                        UserEvent userEvent = new UserEvent(userEventId,now, base64QrCode );

                        userEvent.setUser(customUser);
                        userEvent.setEvent(onlineEvent);

                        userEventService.addUserEventToDb(userEvent);

                        QuestionAnswers answer = new QuestionAnswers(
                                answers, onlineEvent, customUser,now);

                        customUser.addQuestionAnswers(answer);

                        onlineEvent.addQuestionAnswers(answer);

                        questionAnswersRepository.save(answer);


                        customUser.addEventToUser(userEvent);
                        onlineEvent.addUserToEvent(userEvent);

                        userService.updateUser(customUser);
                        onlineEventRepository.save(onlineEvent);

                        javaMailService.mailQrCodeSender(customUser,onlineEvent,base64QrCode);

                        return new MessageResponse(SUCCESS,"YOU HAVE SUCCESSFULLY JOINED THIS EVENT");
                    }else{
                        return new MessageResponse(ERROR,"YOU ALREADY JOINED THIS EVENT");
                    }
                }else{
                    return new MessageResponse(ERROR,"EVENT CANT BE JOINED");
                }
            }else {
                return new MessageResponse(ERROR,"EVENT IS FULL");
            }
        } else if(physicalEventRepository.existsById(eventId)){
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

            if (physicalEvent.isNotFull()) {
                if(physicalEvent.getStartTime().isAfter(now)){
                    if(userEventService.joinedThisEvent(customUser,physicalEvent)){
                        UserEventId userEventId = new UserEventId(customUser.getId(), eventId);

                        String base64QrCode = generateBase64QrCodeForEvent(physicalEvent,customUser,now);

                        UserEvent userEvent = new UserEvent(userEventId,now, base64QrCode );

                        userEvent.setUser(customUser);
                        userEvent.setEvent(physicalEvent);

                        userEventService.addUserEventToDb(userEvent);

                        QuestionAnswers answer = new QuestionAnswers(
                                answers, physicalEvent, customUser,now);


                        customUser.addQuestionAnswers(answer);

                        physicalEvent.addQuestionAnswers(answer);

                        customUser.addEventToUser(userEvent);
                        physicalEvent.addUserToEvent(userEvent);

                        userService.updateUser(customUser);
                        physicalEventRepository.save(physicalEvent);

                        javaMailService.mailQrCodeSender(customUser,physicalEvent,base64QrCode);

                        return new MessageResponse(SUCCESS,"YOU HAVE SUCCESSFULLY JOINED THIS EVENT");
                    }else{
                        return new MessageResponse(ERROR,"YOU ALREADY JOINED THIS EVENT");
                    }
                }else{
                    return new MessageResponse(ERROR,"EVENT CANT BE JOINED");
                }
            } else {
                return new MessageResponse(ERROR,"EVENT IS FULL");
            }
        } else{
            return new MessageResponse(ERROR,"EVENT NOT AVAILABLE");
        }
    }


    @Transactional
    public EventPageRequest getEventPage(Long eventId){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

            Status status;

            if(userEventService.joinedThisEvent(customUser,onlineEvent)){
                status = Status.GOING;
            } else{
                status = Status.NOTHING;
            }

            return new EventPageRequest(onlineEvent, status);
        } else if(physicalEventRepository.existsById(eventId)){
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

            Status status;

            if(userEventService.joinedThisEvent(customUser,physicalEvent)){
                status = Status.GOING;
            } else{
                status = Status.NOTHING;
            }

            return new EventPageRequest(physicalEvent, status);
        } else
        {
            return null;
        }
    }

    @Transactional
    public EventPageRequest getEventPageForMods(Long eventId){
        String username = authenticationFacade.getAuthentication().getName();
        CustomMod customMod = userService.bringMod(username);

        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

            Status status;

            if(onlineEvent.getCreatedBy() == customMod){
                status = Status.OWNER;
            } else{
                status = Status.NOTHING;
            }

            return new EventPageRequest(onlineEvent, status);
        } else if(physicalEventRepository.existsById(eventId)){
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

            Status status;

            if(physicalEvent.getCreatedBy() == customMod){
                status = Status.OWNER;
            } else{
                status = Status.NOTHING;
            }

            return new EventPageRequest(physicalEvent, status);
        } else
        {
            return null;
        }
    }


    @Transactional
    public List<String> getQuestionsToJoin(Long eventId){

        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

            Status status;

            if(!userEventService.joinedThisEvent(customUser,onlineEvent)){
                return null;
            } else{
                return onlineEvent.getQuestions();
            }

        } else if(physicalEventRepository.existsById(eventId)){
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

            Status status;

            if(!userEventService.joinedThisEvent(customUser,physicalEvent)){
                return null;
            } else{
                return physicalEvent.getQuestions();
            }
        } else
        {
            return null;
        }
    }

    @Transactional
    public MessageResponse joinEventWithoutAnswers(Long eventId) throws Exception {
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

            if(!userEventService.joinedThisEvent(customUser,onlineEvent)){
                return new MessageResponse(ERROR,"YOU ALREADY JOINED THIS EVENT");
            } else{
                if(onlineEvent.isNotFull()){
                    if (onlineEvent.getStartTime().isAfter(LocalDateTime.now())) {
                        UserEventId userEventId = new UserEventId(customUser.getId(), eventId);

                        LocalDateTime now = LocalDateTime.now();

                        String base64QrCode = generateBase64QrCodeForEvent(onlineEvent,customUser,now);

                        UserEvent userEvent = new UserEvent(userEventId,now,base64QrCode);

                        userEvent.setEvent(onlineEvent);
                        userEvent.setUser(customUser);

                        userEventService.addUserEventToDb(userEvent);

                        customUser.addEventToUser(userEvent);
                        onlineEvent.addUserToEvent(userEvent);

                        userService.updateUser(customUser);
                        onlineEventRepository.save(onlineEvent);

                        javaMailService.mailQrCodeSender(customUser,onlineEvent,base64QrCode);


                        return new MessageResponse(SUCCESS,"EVENT SUCCESSFULLY JOINED",base64QrCode);
                    } else {
                        return new MessageResponse(ERROR,"EVENT HAS ENDED");
                    }
                }else{
                    return new MessageResponse(ERROR,EVENT_IS_FULL);
                }
            }
        } else if(physicalEventRepository.existsById(eventId)){
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

            if(!userEventService.joinedThisEvent(customUser,physicalEvent)){
                return new MessageResponse(ERROR,"YOU ALREADY JOINED THIS EVENT");
            } else{

                if(physicalEvent.isNotFull()){
                    if (physicalEvent.getStartTime().isAfter(LocalDateTime.now())) {
                        UserEventId userEventId = new UserEventId(customUser.getId(), eventId);

                        LocalDateTime now = LocalDateTime.now();

                        String base64QrCode = generateBase64QrCodeForEvent(physicalEvent,customUser,now);

                        UserEvent userEvent = new UserEvent(userEventId,now,base64QrCode);

                        userEvent.setEvent(physicalEvent);
                        userEvent.setUser(customUser);

                        userEventService.addUserEventToDb(userEvent);


                        customUser.addEventToUser(userEvent);
                        physicalEvent.addUserToEvent(userEvent);

                        userService.updateUser(customUser);
                        physicalEventRepository.save(physicalEvent);

                        javaMailService.mailQrCodeSender(customUser,physicalEvent,base64QrCode);


                        return new MessageResponse(SUCCESS,"SUCCESSFULLY JOINED THE EVENT",base64QrCode);
                    } else {
                        return new MessageResponse(ERROR,"EVENT HAS ENDED");
                    }
                }else{
                    return new MessageResponse(ERROR,EVENT_IS_FULL);
                }
            }
        } else
        {
            return null;
        }
    }

    @Transactional
    public MessageResponse askQuestionForEvent(String question,Long eventId){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        if(onlineEventRepository.existsById(eventId)){
            OnlineEvent onlineEvent = onlineEventRepository.getById(eventId);

            if(onlineEvent.getStartTime().isAfter(LocalDateTime.now())){
                onlineEvent.addQuestionFromUser(question);
                return new MessageResponse(SUCCESS,"SUCCESSFULY ASKED QUESTION");
            }else{
                return new MessageResponse(ERROR,"THIS EVENT HAS ENDED");
            }
        } else if(physicalEventRepository.existsById(eventId)){
            PhysicalEvent physicalEvent = physicalEventRepository.getById(eventId);

            if(physicalEvent.getStartTime().isAfter(LocalDateTime.now())){
                physicalEvent.addQuestionFromUser(question);
                return new MessageResponse(SUCCESS,"SUCCESSFULY ASKED QUESTION");
            }else{
                return new MessageResponse(ERROR,"THIS EVENT HAS ENDED");
            }
        } else {
            return null;
        }
    }

    @Transactional
    public List<EventBoxResponse> getGoingEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);


        return customEventRepository.findAll()
                .stream().filter(event -> !userEventService.joinedThisEvent(customUser,event))
                .map(EventBoxResponse::new)
                .toList();
    }

    @Transactional
    public List<EventBoxResponse> getJoinedEvents(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomUser customUser = userService.bringUser(username);

        var test = customEventRepository.findAllByStartTimeBefore(LocalDateTime.now())
                .stream().filter(event -> !userEventService.joinedThisEvent(customUser, event))
                .map(EventBoxResponse::new)
                .toList();


        return test;
    }

    @Transactional
    public List<EventBoxResponse> getCreatedEventsEnded(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomMod customMod = userService.bringMod(username);

        return customEventRepository.findAllByCreatedByAndStartTimeBefore(customMod,LocalDateTime.now())
                .stream()
                .map(EventBoxResponse::new)
                .toList();
    }

    @Transactional
    public List<EventBoxResponse> getCreatedEventsNotEnded(){
        String username = authenticationFacade.getAuthentication().getName();
        CustomMod customMod = userService.bringMod(username);

        return customEventRepository.findAllByCreatedByAndStartTimeAfter(customMod,LocalDateTime.now())
                .stream()
                .map(EventBoxResponse::new)
                .toList();
    }
}

