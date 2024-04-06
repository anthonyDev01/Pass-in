package anthonydev.com.passin.services;

import anthonydev.com.passin.domain.attendee.Attendee;
import anthonydev.com.passin.domain.event.Event;
import anthonydev.com.passin.domain.event.exceptions.EventFullException;
import anthonydev.com.passin.domain.event.exceptions.EventNotFoundException;
import anthonydev.com.passin.dto.attendee.AttendeeIdDTO;
import anthonydev.com.passin.dto.attendee.AttendeeResquestDTO;
import anthonydev.com.passin.dto.event.EventIdDTO;
import anthonydev.com.passin.dto.event.EventRequestDTO;
import anthonydev.com.passin.dto.event.EventResponseDTO;
import anthonydev.com.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttenderOnEvent(String eventId, AttendeeResquestDTO attendeeResquestDTO) {
        this.attendeeService.verifyAttendeeSubscription(attendeeResquestDTO.email(), eventId);

        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if (event.getMaximumAttendees() <= attendeeList.size()) {
            throw new EventFullException("Event is full");
        }

        Attendee newAttende = new Attendee();
        newAttende.setName(attendeeResquestDTO.name());
        newAttende.setEmail(attendeeResquestDTO.email());
        newAttende.setEvent(event);
        newAttende.setCreatedAT(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttende);

        return new AttendeeIdDTO(newAttende.getId());
    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));
    }

    private String createSlug(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

}
