package qubiqule.cheqr.cheQR.admin.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.VenueDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    String message;
    VenueDTO venue;
}
