package qubiqule.cheqr.cheQR.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionCreationResponse {
    private String qrCodeData;
}
