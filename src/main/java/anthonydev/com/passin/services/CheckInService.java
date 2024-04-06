package anthonydev.com.passin.services;

import anthonydev.com.passin.domain.attendee.Attendee;
import anthonydev.com.passin.domain.chekin.Chekin;
import anthonydev.com.passin.domain.chekin.exceptions.CheckInAlreadyExistsException;
import anthonydev.com.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckinRepository checkinRepository;

    public void registerCheckIn(Attendee attendee){
        this.verifyCheckInExists(attendee.getId());
        Chekin newCheckIn = new Chekin();
        newCheckIn.setAttendee(attendee);
        newCheckIn.setCreatedAt(LocalDateTime.now());
        this.checkinRepository.save(new Chekin());
    }

    private void verifyCheckInExists(String attendeeId){
        Optional<Chekin> isCheckedIn = this.checkinRepository.findByAttendeeId(attendeeId);
        if(isCheckedIn.isPresent()) throw new CheckInAlreadyExistsException("Attendee already checked in");
    }

}
