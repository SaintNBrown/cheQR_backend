package qubiqule.cheqr.cheQR.service.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrCodeNotificationDTO{
    private Long sessionId;
    private String qrData;
}
