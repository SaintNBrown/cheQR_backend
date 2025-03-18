package qubiqule.cheqr.cheQR.payload.response;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class JwtResponse {
    
    private String jwt;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;

    @Getter
    @Setter
    private String firstName;
    
    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String regNumber;

    @Getter
    @Setter
    private String refreshToken;

    @Getter
    private final List<String> roles;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Date expiryDate;

    public JwtResponse(
        String message,
        Long id,
        String firstName,
        String lastName,
        String regNumber,
        String username, 
        String email, 
        List<String> roles, 
        String jwt,
        String refreshToken,
        Date expiryDate

        ) {
        this.message = message;
        this.email = email;
        this.id = id;
        this.roles = roles;
        this.jwt = jwt;
        this.username = username;
        this.refreshToken = refreshToken;
        this.firstName = firstName;
        this.lastName = lastName;
        this.regNumber = regNumber;
        this.expiryDate = expiryDate;
    }

    public String getAccessToken() {
        return jwt;
    }

    public void setAcessToken(String jwt) {
        this.jwt = jwt;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
}
