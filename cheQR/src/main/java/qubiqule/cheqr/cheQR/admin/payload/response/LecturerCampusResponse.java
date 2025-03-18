package qubiqule.cheqr.cheQR.admin.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerCampusResponse {
    String message;
    Lecturer lecturer;
    Campus campus;
}
