package anthonydev.com.passin.services;

import anthonydev.com.passin.domain.attendee.Attendee;
import anthonydev.com.passin.domain.attendee.exception.AttendeeAlreadyExistException;
import anthonydev.com.passin.domain.attendee.exception.AttendeeNotFoundException;
import anthonydev.com.passin.domain.chekin.Chekin;
import anthonydev.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import anthonydev.com.passin.dto.attendee.AttendeeDetails;
import anthonydev.com.passin.dto.attendee.AttendeesListResponseDTO;
import anthonydev.com.passin.dto.attendee.AttendeeBadgeDTO;
import anthonydev.com.passin.repositories.AttendeeRepository;
import anthonydev.com.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId) {
        return this.attendeeRepository.findByEventId(eventId);

    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId) {
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);
        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<Chekin> checkin = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkedInAt = checkin.isPresent() ? checkin.get().getCreatedAt() : null;
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAT(), checkedInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }

    public void verifyAttendeeSubscription(String email, String eventId) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if (isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyExistException("Attendee is already registered");
    }

    public void checkInAttendee(String attendeeId){
        Attendee attedee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attedee);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("attendee not found with ID: " + attendeeId));
    }

    public Attendee registerAttendee(Attendee newAttendee) {
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        Attendee attedee = this.getAttendee(attendeeId);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();
        AttendeeBadgeDTO badgeDTO = new AttendeeBadgeDTO(attedee.getName(), attedee.getEmail(), uri, attedee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(badgeDTO);
    }
}
