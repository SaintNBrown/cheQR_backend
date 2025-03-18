package qubiqule.cheqr.cheQR.admin.payload.response;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.VenueDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCampusVenuesResponse {
    String message;
    Set<VenueDTO> venueDTOs;
}
