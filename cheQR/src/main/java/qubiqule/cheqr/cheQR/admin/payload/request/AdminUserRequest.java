package qubiqule.cheqr.cheQR.admin.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRequest {
    private String username;
    private String email;
    private String password;
}
