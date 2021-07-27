package yte.intern.project.event.service;

import org.springframework.stereotype.Service;
import yte.intern.project.common.dto.MessageResponse;
import yte.intern.project.common.enums.MessageType;
import yte.intern.project.event.controller.request.AddEventRequest;
import yte.intern.project.event.entities.CustomEvent;
import yte.intern.project.event.repository.EventRepository;

import javax.naming.NameNotFoundException;

import java.util.Optional;

import static yte.intern.project.common.enums.MessageType.ERROR;
import static yte.intern.project.common.enums.MessageType.SUCCESS;

@Service
public class CustomEventService {

    private final EventRepository eventRepository;

    private static final String START_CANT_BE_AFTER_END = "START TIME CANNOT BE AFTER END TIME";
    private static final String EVENT_SUCCESSFULLY_ADDED = "EVENT WITH NAME %s HAS BEEN SUCCESSFULLY ADDED TO THE DATABASE";
    private static final String EVENT_ALREADY_EXISTS = "EVENT WITH THE NAME %s DOES ALREADY EXIST. PLEASE CHOOSE ANOTHER NAME FOR NOT ANY CONFUSION";


    public CustomEventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public CustomEvent getEventbyeventName(String eventName) throws NameNotFoundException {
        return eventRepository.findByEventName(eventName)
                .orElseThrow(()->new NameNotFoundException("BELİRTİLEN ETKİNLİK BULUNAMADI"));
    }

    public CustomEvent getEventbyId(Long id) throws Exception {
        return eventRepository.findById(id)
                .orElseThrow(()-> new Exception("ETKİNLİK BULUNAMADI"));
    }

    public MessageResponse addEventToDb(AddEventRequest addEventRequest){
        if(!eventRepository.existsByEventName(addEventRequest.getEventName())){
            if(addEventRequest.getStartTime().isAfter(addEventRequest.getEndTime())){
                return new MessageResponse(ERROR,
                        START_CANT_BE_AFTER_END);
            }
            else{
                CustomEvent customEvent = addEventRequest.toEvent();
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

    public Optional<CustomEvent> loadByEventName(String eventName){
        return eventRepository.findByEventName(eventName);
    }
}
