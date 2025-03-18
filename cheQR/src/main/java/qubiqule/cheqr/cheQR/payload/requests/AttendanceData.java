package qubiqule.cheqr.cheQR.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceData {

    String qrCodeString;

    String studentLongitude;

    String studentLatitude;

    String studentAltitude;

    @Setter
    @Getter
    String studentId;
}
