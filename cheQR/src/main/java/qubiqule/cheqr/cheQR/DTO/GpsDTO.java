package qubiqule.cheqr.cheQR.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GpsDTO {
    private double latitude;

    private double longitude;

    private double altitude;
}
