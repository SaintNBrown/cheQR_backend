package qubiqule.cheqr.cheQR.DTO.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.DTO.AttendanceListDTO;
import qubiqule.cheqr.cheQR.DTO.CourseDTO;
import qubiqule.cheqr.cheQR.models.business.ClassSessionStatus;

@Data
@NoArgsConstructor
public class SessionRequest {
    LocalDate date;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
    CourseDTO course;
    AttendanceListDTO attendanceListDTO;
    String venueId;
    ClassSessionStatus classSessionStatus;
    boolean isActive;
}
