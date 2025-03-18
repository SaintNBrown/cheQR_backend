package qubiqule.cheqr.cheQR.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.business.Venue;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    boolean existsByHallName(String hallName);

    Optional<Venue> findByHallName(String hallName);

    Optional<Venue> findByHallNameAndCampusId(String hallName, Long campusId);

}
