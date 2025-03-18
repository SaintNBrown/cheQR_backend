package qubiqule.cheqr.cheQR.admin.payload.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerByCampusResponse {
    String message;
    Set<LecturerDTO> lecturerDTOs;
}
