package anthonydev.com.passin.repositories;

import anthonydev.com.passin.domain.chekin.Chekin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckinRepository extends JpaRepository<Chekin, Integer> {
}
