package anthonydev.com.passin.services;

import anthonydev.com.passin.domain.attendee.Attendee;
import anthonydev.com.passin.domain.event.Event;
import anthonydev.com.passin.dto.event.EventResponseDTO;
import anthonydev.com.passin.repositories.AttendeeRepository;
import anthonydev.com.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private  final AttendeeRepository attendeeRepository;

    public EventResponseDTO getEventDetail(String eventId){
        Event event =  this.eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with ID: " + eventId));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

}
