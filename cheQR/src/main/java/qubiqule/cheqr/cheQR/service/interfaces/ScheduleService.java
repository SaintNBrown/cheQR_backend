package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;

import qubiqule.cheqr.cheQR.DTO.schedule.ScheduleRequest;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.business.Schedule;

/**
 * 
 * @param lecturerId
 * @return
 */
public interface ScheduleService {
    Schedule createSchedule(Long lecturerId, ScheduleRequest scheduleRequest);
    ClassSession addClassSessionToSchedule(Long scheduleId, ClassSession session);
    List<ClassSession> getClassSessionsBySchedule(Long scheduleId);
    List<Schedule> getLecturerSchedules(Long lecturerId);
    void deleteSchedule(Long scheduleId);
}
