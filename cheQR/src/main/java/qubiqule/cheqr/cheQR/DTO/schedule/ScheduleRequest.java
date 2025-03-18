package qubiqule.cheqr.cheQR.DTO.schedule;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScheduleRequest {
    String title;
    List<SessionRequest> sessionRequestsList;
}
