package qubiqule.cheqr.cheQR.payload.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentRequest{
    private String registrationNumber;
    private String courseCode;
}