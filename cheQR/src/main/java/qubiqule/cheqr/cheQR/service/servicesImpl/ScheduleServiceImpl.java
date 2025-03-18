package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.DTO.schedule.ScheduleRequest;
import qubiqule.cheqr.cheQR.DTO.schedule.SessionRequest;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.business.ClassSessionStatus;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.business.Schedule;
import qubiqule.cheqr.cheQR.models.business.Venue;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.repository.ClassSessionRepository;
import qubiqule.cheqr.cheQR.repository.LecturerRepository;
import qubiqule.cheqr.cheQR.repository.ScheduleRepository;
import qubiqule.cheqr.cheQR.service.interfaces.AttendanceService;
import qubiqule.cheqr.cheQR.service.interfaces.ClassSessionService;
import qubiqule.cheqr.cheQR.service.interfaces.CourseService;
import qubiqule.cheqr.cheQR.service.interfaces.ScheduleService;
import qubiqule.cheqr.cheQR.service.interfaces.VenueService;

@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private VenueService venueService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ClassSessionService classSessionService;

    @Override
    @Transactional
    public Schedule createSchedule(Long lecturerId, ScheduleRequest scheduleRequest) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Lecturer not found"));

        Schedule schedule = new Schedule();
        schedule.setTitle(scheduleRequest.getTitle());
        schedule.setLecturer(lecturer);
        schedule = scheduleRepository.save(schedule);

        List<ClassSession> classSessions = new ArrayList<>();

        for (SessionRequest sessionRequest : scheduleRequest.getSessionRequestsList()) {
            Course course = courseService.findByCourseCode(sessionRequest.getCourse().getCourseCode());
            Venue venue = venueService.getVenueFromId(Long.parseLong(sessionRequest.getVenueId()));

            // Create and save ClassSession first
            ClassSession classSession = new ClassSession();
            classSession.setCourse(course);
            classSession.setDayOfWeek(sessionRequest.getDayOfWeek());
            classSession.setStartTime(sessionRequest.getStartTime());
            classSession.setEndTime(sessionRequest.getEndTime());
            classSession.setStatus(ClassSessionStatus.SCHEDULED);
            classSession.setActive(false);
            classSession.setSchedule(schedule);
            classSession.setDate(sessionRequest.getDate());
            classSession.setVenue(venue);

            // Save ClassSession to get ID
            classSession = classSessionRepository.save(classSession);

            // Create AttendanceList with saved ClassSession
            AttendanceList attendanceList = new AttendanceList();
            attendanceList.setClassSession(classSession); // Set ClassSession before saving
            attendanceService.saveAttendanceList(attendanceList);

            // Set back to ClassSession and save again
            classSession.setAttendanceList(attendanceList);
            classSession = classSessionRepository.save(classSession);

            classSessions.add(classSession);
            course.getClassSessions().add(classSession);
        }

        schedule.setClassSessions(classSessions);
        schedule = scheduleRepository.save(schedule);
        generateSessionsQrCodeString(schedule);
        return schedule;
    }

    private void generateSessionsQrCodeString(Schedule schedule) {
        schedule.getClassSessions().forEach((session) -> {
            try {
                session.setQrCodeString(
                    "SessionId:"+ session.getId() +";courseCode:" + session.getCourse().getCourseCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public ClassSession addClassSessionToSchedule(Long scheduleId, ClassSession session) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        session.setSchedule(schedule);
        return classSessionRepository.save(session);
    }

    @Override
    public List<ClassSession> getClassSessionsBySchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        return schedule.getClassSessions();
    }

    @Override
    public List<Schedule> getLecturerSchedules(Long lecturerId) {
        log.info("Lecturer schedules: ");
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Lecturer not found!"));

        List<Schedule> lecturerSchedules = lecturer.getSchedules();

        log.info("Lecturer schedules: ", lecturer.getSchedules().toString());
        return lecturerSchedules;
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new EntityNotFoundException("Schedule not found!"));

        if (schedule.getClassSessions() != null) {
            schedule.getClassSessions().forEach(session -> {
                session.setCourse(null);
                session.setVenue(null);
                session.setSchedule(null);
                classSessionService.deleteClassSession(session.getId().toString());
            });
            schedule.setClassSessions(null);
        }
        scheduleRepository.deleteById(scheduleId);
    }
}