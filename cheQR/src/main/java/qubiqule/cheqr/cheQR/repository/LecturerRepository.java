package qubiqule.cheqr.cheQR.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
        // Find a lecturer by their username
        Optional<Lecturer> findByUsername(String username);

        // Check if a lecturer exists by their username
        boolean existsByUsername(String username);
}
