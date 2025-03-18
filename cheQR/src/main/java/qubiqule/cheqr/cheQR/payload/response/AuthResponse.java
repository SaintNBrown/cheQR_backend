package qubiqule.cheqr.cheQR.payload.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    private List<String> roles;

    public AuthResponse( String token, String refreshToken, List<String> roles) {
        this.refreshToken = refreshToken;
        this.roles = roles;
        this.token = token;
    }


}
