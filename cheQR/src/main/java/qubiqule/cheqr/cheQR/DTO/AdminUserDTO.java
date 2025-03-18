package qubiqule.cheqr.cheQR.DTO;

import lombok.Getter;
import lombok.Setter;
import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;

public class AdminUserDTO extends UserDTO{

    @Getter
    @Setter
    private InstitutionDTO institutionDTO;
}
