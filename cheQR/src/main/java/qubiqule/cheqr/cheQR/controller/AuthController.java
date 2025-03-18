package qubiqule.cheqr.cheQR.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.DTO.LoginRequest;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.payload.requests.SignUpRequest;
import qubiqule.cheqr.cheQR.payload.response.JwtResponse;
import qubiqule.cheqr.cheQR.payload.response.SignupResponse;
import qubiqule.cheqr.cheQR.repository.RoleRepository;
import qubiqule.cheqr.cheQR.repository.UserRepository;
import qubiqule.cheqr.cheQR.security.jwt.JwtUtils;
import qubiqule.cheqr.cheQR.security.services.UserDetailsImpl;
import qubiqule.cheqr.cheQR.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@Slf4j
//@CrossOrigin(origins = "http://192.168.89.205:8100")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtils jwtUtils;
    private final AuthService authService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            AuthService authService) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Recieved a Login request: " + loginRequest.toString());
            String jwt = authService.authenticate(loginRequest);

            UserDetailsImpl userDetails = authService.getUserDetails(loginRequest.getUsername());

            List<String> role = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            User user = authService.getUserByEmail(userDetails.getEmail());
            String firstName = "";
            String lastName = "";
            String regNumber = "";
            if (user != null) {
                firstName = user.getFirstName();
                lastName = user.getLastName();
                regNumber = role.contains("ROLE_STUDENT") ? ((Student) user).getRegNumber() : null;
            }
            return ResponseEntity.ok(
                    new JwtResponse("Login successful!", userDetails.getId(), firstName, lastName, regNumber,
                            userDetails.getUsername(), userDetails.getEmail(), role, jwt, jwtUtils.refreshToken(jwt),
                            jwtUtils.extractExpiration(jwt)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignUpRequest signupRequest)
            throws Exception {
        logger.info("Recieved a signup request for username: {}", signupRequest.getUsername());

        this.authService.validateSignupRequest(signupRequest);

        User user = authService.createNewUser(signupRequest);

        return ResponseEntity.ok(new SignupResponse("User registered successfully! ", user));
    }

    public boolean usernameAlreadyExists(SignUpRequest signupRequest) {
        return authService.usernameExists(signupRequest.getUsername());
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = authService.getUserByEmail(email);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found: {}", email);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token is missing");
        }

        try {
            // Validate the refresh token
            if (!jwtUtils.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            // Generate a new access token
            String newAccessToken = jwtUtils.refreshToken(refreshToken);

            // Optional: Return additional metadata if needed
            Map<String, Object> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("expiresIn", jwtUtils.extractExpiration(refreshToken));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle exceptions like token expiration or manipulation attempts
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token error: " + e.getMessage());
        }

    }

    @GetMapping("/userExists")
    public ResponseEntity<Boolean> userExists(@RequestParam String username){
        return this.authService.userExists(username);
    }

}
