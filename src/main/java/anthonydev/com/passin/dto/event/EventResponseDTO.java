package anthonydev.com.passin.dto.event;

import anthonydev.com.passin.domain.event.Event;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventResponseDTO {
    EventDetailDTO event;

    public EventResponseDTO(Event event, Integer numberOfAttendees) {
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), numberOfAttendees);
    }
}
