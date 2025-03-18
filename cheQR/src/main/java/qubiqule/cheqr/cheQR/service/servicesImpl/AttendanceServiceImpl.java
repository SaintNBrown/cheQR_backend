package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.repository.AttendanceListRepository;
import qubiqule.cheqr.cheQR.repository.AttendanceRecordRepository;
import qubiqule.cheqr.cheQR.repository.ClassSessionRepository;
import qubiqule.cheqr.cheQR.repository.StudentRepository;
import qubiqule.cheqr.cheQR.service.interfaces.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    private AttendanceListRepository attendanceListRepository;

    @Override
    public AttendanceRecord markAttendance(Long sessionId, Long studentId) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Class Session not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!session.isActive()) {
            throw new IllegalStateException("Class session is not active for attendance");
        }

        // Find or create the AttendanceList for the session
        AttendanceList attendanceList = session.getAttendanceList();
        if (attendanceList == null) {
            attendanceList = new AttendanceList();
            attendanceList.setClassSession(session);
            attendanceListRepository.save(attendanceList);
            session.setAttendanceList(attendanceList);
            classSessionRepository.save(session);
        }

        // Create a new AttendanceRecord
        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setStudent(student);
        attendanceRecord.setAttendanceList(attendanceList);

        return attendanceRecordRepository.save(attendanceRecord);
    }

    @Override
    public List<AttendanceRecord> getAttendanceRecordsBySession(Long sessionId) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Class Session not found"));

        AttendanceList attendanceList = session.getAttendanceList();
        if (attendanceList == null) {
            return new ArrayList<>();
        }

        return attendanceRecordRepository.findByAttendanceList(attendanceList);
    }

    @Override
    public AttendanceList getAttendanceListBySession(Long sessionId){
        ClassSession classSession = classSessionRepository.findById(sessionId)
            .orElseThrow(() -> new EntityNotFoundException("Session not found!"));
            System.out.println("Session Id recieved: " + sessionId);

        AttendanceList attendanceList = classSession.getAttendanceList();

        return attendanceList;
    }

    @Override
    public AttendanceList generateAttendanceList(Long sessionId) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Class Session not found"));

        if (session.isActive()) {
            throw new IllegalStateException("Class session is still active. Cannot generate attendance list.");
        }

        AttendanceList attendanceList = session.getAttendanceList();
        if (attendanceList == null) {
            throw new IllegalStateException("No attendance list found for this session.");
        }

        return attendanceList;
    }

    @Override
    public AttendanceList saveAttendanceList(AttendanceList attendanceList){
        return attendanceListRepository.save(attendanceList);
    }
}
