package qubiqule.cheqr.cheQR.DTO;

import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.VenueDTO;
import qubiqule.cheqr.cheQR.models.business.ClassSessionStatus;

@Data
@NoArgsConstructor
public class SessionDTO {
    String id;
    String date;
    String dayOfWeek;
    LocalTime startTime;
    LocalTime endTime; 
    CourseDTO courseDTO;
    AttendanceListDTO attendanceListDTO; 
    VenueDTO venueDTO;
    ClassSessionStatus status;
    String isActive;
    String qrCodeString;
}
