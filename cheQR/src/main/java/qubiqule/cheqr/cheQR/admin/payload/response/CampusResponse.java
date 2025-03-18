package qubiqule.cheqr.cheQR.admin.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampusResponse {
    String message;
    CampusDTO campusDTO;
}
