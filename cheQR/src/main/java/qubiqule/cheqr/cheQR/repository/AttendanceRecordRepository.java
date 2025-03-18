package qubiqule.cheqr.cheQR.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByAttendanceList(AttendanceList attendanceList);
}
