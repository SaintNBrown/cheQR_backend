package qubiqule.cheqr.cheQR.service.servicesImpl.special_implementation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.repository.ClassSessionRepository;
import qubiqule.cheqr.cheQR.service.interfaces.special_purpose.NotificationService;
import qubiqule.cheqr.cheQR.service.interfaces.special_purpose.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private NotificationService notificationService;

    private final Duration PRE_NOTIFICATION_DURATION = Duration.ofMinutes(5);

    @Transactional
    @Override
    @Scheduled(fixedRate = 5000) // Runs every 5 seconds
    public void checkAndManageSessions() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        LocalTime preNotificationTime = now.plus(PRE_NOTIFICATION_DURATION);

        // Notify lecturers 5 minutes before session starts
        List<ClassSession> sessionsToNotify = classSessionRepository
                .findByStartTimeBetweenAndIsActiveFalseAndNotifiedBeforeStartFalse(now, preNotificationTime);
        for (ClassSession session : sessionsToNotify) {
            notificationService.sendPreNotification(session);
            session.setNotifiedBeforeStart(true);
            classSessionRepository.save(session);
        }

        // Start sessions if their start time is reached
        List<ClassSession> sessionsToStart = classSessionRepository.findByStartTimeEqualsAndIsActiveFalse(now);
        for (ClassSession session : sessionsToStart) {
            notificationService.sendStartNotification(session);
            session.setActive(true);
            classSessionRepository.save(session);
        }

        // End sessions if their end time is reached
        List<ClassSession> sessionsToEnd = classSessionRepository.findByEndTimeBeforeAndIsActiveTrueAndHasEndedFalse(now);
        for (ClassSession session : sessionsToEnd) {
            notificationService.sendEndNotification(session);
            session.setHasEnded(true);
        }
    }
}
