package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;

import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;

public interface AttendanceService {
    AttendanceRecord markAttendance(Long sessionId, Long studentId);
    List<AttendanceRecord> getAttendanceRecordsBySession(Long sessionId);
    AttendanceList getAttendanceListBySession(Long sessionId);
    AttendanceList generateAttendanceList(Long sessionId);
    AttendanceList saveAttendanceList(AttendanceList attendanceList);
}
