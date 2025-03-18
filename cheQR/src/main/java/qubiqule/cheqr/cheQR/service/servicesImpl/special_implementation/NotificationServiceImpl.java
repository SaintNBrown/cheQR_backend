package qubiqule.cheqr.cheQR.service.servicesImpl.special_implementation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.service.EventNotificationService;
import qubiqule.cheqr.cheQR.service.interfaces.special_purpose.NotificationService;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private MappingUtil mappingUtil;

    @Autowired
    private EventNotificationService eventNotificationService;

    @Override
    public void sendPreNotification(ClassSession session) {
        if (session == null) {
            log.warn("Cannot send pre-notification: session is null");
            return;
        }

        if (session.getSchedule() == null || session.getSchedule().getLecturer() == null) {
            log.warn("Cannot send pre-notification: schedule or lecturer is null for session ID {}", session.getId());
            return;
        }

        Long lecturerId = session.getSchedule().getLecturer().getId();
        if (lecturerId == null) {
            log.warn("Cannot send pre-notification: lecturer ID is null for session ID {}", session.getId());
            return;
        }

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("type", "PRE_NOTIFICATION");
        notificationData.put("message", "Class session starting in 5 minutes!");

        // Map session details, handle null from the mapping utility
        Object sessionDetails = mappingUtil.mapToSessionDTO(session);
        if (sessionDetails == null) {
            log.warn("Cannot send pre-notification: session details could not be mapped for session ID {}",
                    session.getId());
            return;
        }
        notificationData.put("sessionDetails", sessionDetails);

        // Send the notification
        eventNotificationService.sendNotification(lecturerId, notificationData);
        log.info("Pre-notification sent to lecturer ID {} for session ID {}", lecturerId, session.getId());
    }

    @Override
    public void sendStartNotification(ClassSession session) {
        if (session == null) {
            log.warn("Cannot send start notification: session is null");
            return;
        }

        if (session.getSchedule() == null || session.getSchedule().getLecturer() == null) {
            log.warn("Cannot send start notification: schedule or lecturer is null for session ID {}", session.getId());
            return;
        }

        Long lecturerId = session.getSchedule().getLecturer().getId();
        if (lecturerId == null) {
            log.warn("Cannot send start notification: lecturer ID is null for session ID {}", session.getId());
            return;
        }

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("type", "START_NOTIFICATION");
        notificationData.put("message", "Class session has started!");

        // Map session details, handle null from the mapping utility
        Object sessionDetails = mappingUtil.mapToSessionDTO(session);
        if (sessionDetails == null) {
            log.warn("Cannot send start notification: session details could not be mapped for session ID {}",
                    session.getId());
            return;
        }
        notificationData.put("sessionDetails", sessionDetails);

        // Send the notification
        eventNotificationService.sendNotification(lecturerId, notificationData);
        log.info("Start notification sent to lecturer ID {} for session ID {}", lecturerId, session.getId());
    }

    @Override
    public void sendEndNotification(ClassSession session) {
        if (session == null) {
            log.warn("Cannot send end-notification: session is null");
            return;
        }

        if (session.getSchedule() == null || session.getSchedule().getLecturer() == null) {
            log.warn("Cannot send end-notification: schedule or lecturer is null for session ID {}", session.getId());
            return;
        }

        Long lecturerId = session.getSchedule().getLecturer().getId();
        if (lecturerId == null) {
            log.warn("Cannot send end-notification: lecturer ID is null for session ID {}", session.getId());
            return;
        }

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("type", "END_NOTIFICATION");
        notificationData.put("message", "Class session has ended. Attendance list generated.");

        // Map attendance list, handle null from the mapping utility
        Object attendanceList = mappingUtil.mapToAttendanceListDTO(session.getAttendanceList());
        if (attendanceList == null) {
            log.warn("Cannot send end-notification: attendance list could not be mapped for session ID {}",
                    session.getId());
            return;
        }
        notificationData.put("attendanceList", attendanceList);

        // Send the notification
        eventNotificationService.sendNotification(lecturerId, notificationData);
        log.info("End-notification sent to lecturer ID {} for session ID {}", lecturerId, session.getId());
    }

}
