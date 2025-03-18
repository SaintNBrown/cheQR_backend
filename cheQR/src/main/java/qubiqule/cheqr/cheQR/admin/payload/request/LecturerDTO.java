package qubiqule.cheqr.cheQR.admin.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerDTO {
    String id;
    String username;
    String firstName;
    String lastName;
    String email;
}
