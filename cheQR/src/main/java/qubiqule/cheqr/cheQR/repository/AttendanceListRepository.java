package qubiqule.cheqr.cheQR.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.ClassSession;

@Repository
public interface AttendanceListRepository extends JpaRepository<AttendanceList, Long> {
    Optional<AttendanceList> findByClassSession(ClassSession classSession);
}
