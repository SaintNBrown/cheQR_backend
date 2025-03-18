package qubiqule.cheqr.cheQR.admin.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampusByInstitutionResponse {
    String message;
    List<CampusDTO> campuses;
}
