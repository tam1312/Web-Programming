package mk.ukim.finki.wp.jan2021.events.service.impl;

import mk.ukim.finki.wp.jan2021.events.model.Event;
import mk.ukim.finki.wp.jan2021.events.model.EventType;
import mk.ukim.finki.wp.jan2021.events.model.exceptions.InvalidEventIdException;
import mk.ukim.finki.wp.jan2021.events.repository.EventRepository;
import mk.ukim.finki.wp.jan2021.events.service.EventLocationService;
import mk.ukim.finki.wp.jan2021.events.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventLocationService eventLocationService;

    public EventServiceImpl(EventRepository eventRepository, EventLocationService eventLocationService) {
        this.eventRepository = eventRepository;
        this.eventLocationService = eventLocationService;
    }

    @Override
    public List<Event> listAllEvents() {
        return this.eventRepository.findAll();
    }

    @Override
    public Event findById(Long id) {
        return this.eventRepository.findById(id).orElseThrow(InvalidEventIdException::new);
    }

    @Override
    public Event create(String name, String description, Double price, EventType type, Long location) {
        Event event = new Event(name, description, price, type, this.eventLocationService.findById(location));
        return this.eventRepository.save(event);
    }

    @Override
    public Event update(Long id, String name, String description, Double price, EventType type, Long location) {
        Event event = this.findById(id);
        event.setName(name);
        event.setDescription(description);
        event.setPrice(price);
        event.setType(type);
        event.setLocation(this.eventLocationService.findById(location));

        return this.eventRepository.save(event);
    }

    @Override
    public Event delete(Long id) {
        Event event = this.findById(id);
        this.eventRepository.delete(event);
        return event;
    }

    @Override
    public Event like(Long id) {
        Event event = this.findById(id);
        event.setLikes(event.getLikes() + 1);
        return this.eventRepository.save(event);
    }

    @Override
    public List<Event> listEventsWithPriceLessThanAndType(Double price, EventType type) {
        if(price != null && type != null){
            return this.eventRepository.findAllByPriceLessThanAndType(price, type);
        }else if(price != null){
            return this.eventRepository.findAllByPriceLessThan(price);
        }else if(type != null){
            return this.eventRepository.findAllByType(type);
        }else{
            return this.eventRepository.findAll();
        }
    }
}
