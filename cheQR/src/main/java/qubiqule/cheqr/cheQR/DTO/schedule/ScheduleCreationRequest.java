package qubiqule.cheqr.cheQR.DTO.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleCreationRequest {
    String lecturerId;
    ScheduleRequest scheduleRequest;
}
