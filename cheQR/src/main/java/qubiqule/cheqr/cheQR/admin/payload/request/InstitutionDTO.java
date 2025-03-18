package qubiqule.cheqr.cheQR.admin.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionDTO {
    Long id;
    String name;
    List<CampusDTO> campuses;
}
