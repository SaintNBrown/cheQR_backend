package qubiqule.cheqr.cheQR.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.models.business.AttendanceStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceRecordsDTO {
    String id;
    StudentDTO studentDTO;
    AttendanceStatus attendanceStatus;
}
