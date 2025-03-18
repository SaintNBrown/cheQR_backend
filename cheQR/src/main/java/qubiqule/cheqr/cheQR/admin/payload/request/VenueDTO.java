package qubiqule.cheqr.cheQR.admin.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.DTO.GpsDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueDTO {
    String venueId;
    String hallname;
    GpsDTO firstCornerCordinates;
    GpsDTO secondCornerCordinates;
    GpsDTO thirdCornerCordinates;
    GpsDTO fourthCornerCordinates;
    Long campusId;
    
}
