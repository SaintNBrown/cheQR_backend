package qubiqule.cheqr.cheQR.DTO.schedule;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.DTO.SessionDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    String id;
    String title;
    List<SessionDTO> classSessions;
}
