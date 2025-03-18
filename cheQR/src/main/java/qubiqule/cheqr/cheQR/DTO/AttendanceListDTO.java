package qubiqule.cheqr.cheQR.DTO;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceListDTO {
    String id;
    List<AttendanceRecordsDTO> attendanceRecordDTOs;
}
