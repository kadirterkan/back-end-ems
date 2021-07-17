package yte.intern.project.event.service;

import org.hibernate.annotations.NotFound;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import yte.intern.project.event.entities.Event;
import yte.intern.project.event.repository.EventRepository;

import javax.naming.NameNotFoundException;

@Service
public class EventService {

    private final EventRepository eventRepository;


    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventbyeventName(String eventName) throws NameNotFoundException {
        return eventRepository.findByEventName(eventName)
                .orElseThrow(()->new NameNotFoundException("BELİRTİLEN ETKİNLİK BULUNAMADI"));
    }

    public Event getEventbyId(Long id) throws Exception {
        return eventRepository.findById(id)
                .orElseThrow(()-> new Exception("ETKİNLİK BULUNAMADI"));
    }
}
