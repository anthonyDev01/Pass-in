package anthonydev.com.passin.repositories;

import anthonydev.com.passin.domain.chekin.Chekin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<Chekin, Integer> {
    Optional<Chekin> findByAttendeeId(String attendeeId);
}
