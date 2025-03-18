package qubiqule.cheqr.cheQR.admin.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionUpdateResponse {
    String message;
    InstitutionDTO institutionDTO;
}
