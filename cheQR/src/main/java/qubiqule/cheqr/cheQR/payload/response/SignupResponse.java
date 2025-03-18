package qubiqule.cheqr.cheQR.payload.response;

import java.util.Map;

import lombok.Data;
import qubiqule.cheqr.cheQR.models.User;

@Data
public class SignupResponse {
    private String message;
    private User user;
    private Map<String, String> errors;

    public SignupResponse(String message, User user) {
        this.errors = null;
        this.message = message;
        this.user = user;
    }

    public SignupResponse(Map<String, String> errors) {
        this.errors = errors;
        this.message = "Validation errors occured!";
        this.user =  null;
    }



}
