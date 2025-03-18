package qubiqule.cheqr.cheQR.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Institution;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findByCampusName(String name);

    Optional<Campus> findByCampusNameAndInstitution(String name, Institution institution);

    // Custom method to delete a campus by name
    @Modifying
    @Transactional
    @Query("DELETE FROM Campus c WHERE c.id = :id")
    void deleteById(@Param("id") @NonNull Long id);
}
