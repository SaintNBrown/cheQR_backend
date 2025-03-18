package qubiqule.cheqr.cheQR.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.DTO.LoginRequest;
import qubiqule.cheqr.cheQR.admin.AdminUser;
import qubiqule.cheqr.cheQR.admin.AdminUserRepository;
import qubiqule.cheqr.cheQR.exceptions.RoleNotFoundException;
import qubiqule.cheqr.cheQR.models.ERole;
import qubiqule.cheqr.cheQR.models.Role;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.payload.requests.SignUpRequest;
import qubiqule.cheqr.cheQR.repository.LecturerRepository;
import qubiqule.cheqr.cheQR.repository.RoleRepository;
import qubiqule.cheqr.cheQR.repository.StudentRepository;
import qubiqule.cheqr.cheQR.repository.UserRepository;
import qubiqule.cheqr.cheQR.security.jwt.JwtUtils;
import qubiqule.cheqr.cheQR.security.services.UserDetailsImpl;
import qubiqule.cheqr.cheQR.service.business.BusinessService;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);
    @Autowired
    private UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final DaoAuthenticationProvider authenticationManager;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private UserService userService;

    public AuthService(
            DaoAuthenticationProvider authenticationManager,
            JwtUtils jwtUtils,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            StudentRepository studentRepository,
            LecturerRepository lecturerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @Transactional
    public String authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Object principal = authentication.getPrincipal();

            String username = ((UserDetailsImpl) principal).getUsername();

            return jwtUtils.generateToken(username);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw e;
        }
    }

    public UserDetailsImpl getUserDetails(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public void validateSignupRequest(SignUpRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new Error("Error: Username already taken!");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new Error("Error: Email is already taken");
        }

        if (isBlank(signupRequest.getUsername())) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (isBlank(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }

    @Transactional
    public User createNewUser(SignUpRequest signUpRequest) {
        User user = switch (mapToERole(signUpRequest.getRole().stream().findFirst().get())) {
            case ROLE_STUDENT -> {
                Student student = new Student();
                student.setRegNumber(signUpRequest.getRegNumber());
                yield student;
            }
            case ROLE_LECTURER -> new Lecturer();
            case ROLE_ADMIN -> new AdminUser();
            default -> throw new RoleNotFoundException("Unsupported role");
        };
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        List<Role> roles = roleRepository.findAllByName(
                signUpRequest.getRole().stream().map(ERole::valueOf).collect(Collectors.toList()));
        if (roles.isEmpty()) {
            throw new RoleNotFoundException("Roles not found: " + signUpRequest.getRole());
        }
        user.setRoles(new HashSet<>(roles));
        if (user instanceof Student student) {
            studentRepository.save(student);
        } else if (user instanceof Lecturer lecturer) {
            lecturerRepository.save(lecturer);
        } else if (user instanceof AdminUser adminUser) {
            adminUserRepository.save(adminUser);
        }
        return userRepository.save(user);
    }

    private ERole mapToERole(String role) {
        return ERole.valueOf(role.toUpperCase().trim());
    }

    public ResponseEntity<Boolean> userExists(String username) {
        return this.userService.userExists(username);
    }
}