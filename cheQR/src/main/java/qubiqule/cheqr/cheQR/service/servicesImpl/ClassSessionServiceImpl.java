package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.repository.ClassSessionRepository;
import qubiqule.cheqr.cheQR.service.UserService;
import qubiqule.cheqr.cheQR.service.interfaces.ClassSessionService;

@Slf4j
@Service
public class ClassSessionServiceImpl implements ClassSessionService {
    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private UserService userService;

    @Override
    public ClassSession startClassSession(Long sessionId) throws Exception {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Class Session not found"));
        session.setActive(true);
        return classSessionRepository.save(session);
    }

    @Override
    public ClassSession endClassSession(Long sessionId) {
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Class Session not found"));
        session.setActive(false);
        return classSessionRepository.save(session);
    }

    @Override
    @Transactional
    public ClassSession updateClassSession(ClassSession updatedClassSession) throws UserPrincipalNotFoundException {
        Optional<ClassSession> optionalClassSession = classSessionRepository.findById(updatedClassSession.getId());

        // Check if class session is found
        if (optionalClassSession.isPresent()) {
            ClassSession classSession = optionalClassSession.get();

            // Update fields only if they are not null
            if (updatedClassSession.getStartTime() != null) {
                classSession.setStartTime(updatedClassSession.getStartTime());
            }
            // Assuming there's no endTime field, remove this line if it doesn't exist
            if (updatedClassSession.getEndTime() != null) {
                classSession.setEndTime(updatedClassSession.getEndTime());
            }
            if (updatedClassSession.getCourse() != null) {
                classSession.setCourse(updatedClassSession.getCourse());
            }
            if (updatedClassSession.getQrCodeString() != null) {
                classSession.setQrCodeString(updatedClassSession.getQrCodeString());
            }
            if (updatedClassSession.getAttendanceList() != null) {
                classSession.setAttendanceList(updatedClassSession.getAttendanceList());
            }
            if (updatedClassSession.getSchedule() != null) {
                classSession.setSchedule(updatedClassSession.getSchedule());
            }
            if (updatedClassSession.getStatus() != null) {
                classSession.setStatus(updatedClassSession.getStatus());
            }

            // Save the modified class session
            return classSessionRepository.save(classSession);
        } else {
            throw new EntityNotFoundException("Class session not found.");
        }
    }

    @Override
    public List<ClassSession> getCurrentClassSessions() throws UserPrincipalNotFoundException {
        Lecturer lecturer = (Lecturer) userService.getCurrentUser();

        return classSessionRepository.findOngoingSessionsForLecturer(lecturer.getId(), LocalDate.now(), LocalTime.now());
        
    }

    @Override
    public ClassSession getClassSession(Long sessionId) {
        ClassSession classSession = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

        return classSession;
    }

    @Override
    @Transactional
    public void deleteClassSession(String sessionId) {
        ClassSession classSession = classSessionRepository.findById(Long.parseLong(sessionId))
                .orElseThrow(() -> new EntityNotFoundException("Entity not found!"));

        classSession.setCourse(null);
        classSession.setVenue(null);
        classSession.setSchedule(null);
        classSessionRepository.delete(classSession);
    }
}
