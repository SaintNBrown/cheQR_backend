package qubiqule.cheqr.cheQR.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.models.business.Institution;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findByInstitutionNameIgnoreCase(String institutionName);

    @Modifying
    @Transactional
    @Query("DELETE FROM Institution i WHERE i.id = :id")
    void deleteByName(@Param("id") Long id);
}
