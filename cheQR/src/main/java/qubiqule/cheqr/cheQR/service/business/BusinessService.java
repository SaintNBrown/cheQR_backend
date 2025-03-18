package qubiqule.cheqr.cheQR.service.business;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.DTO.StudentDTO;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.Role;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.payload.requests.CourseRequest;
import qubiqule.cheqr.cheQR.security.jwt.JwtUtils;
import qubiqule.cheqr.cheQR.service.UserService;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;
import qubiqule.cheqr.cheQR.service.interfaces.StudentService;

@Slf4j
@Service
public class BusinessService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MappingUtil mappingUtil;

    public ResponseEntity<List<CourseRequest>> getUserCourses(HttpServletRequest request) {

        try {
            String token = request.getHeader("Authorization");

            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(List.of());
            }

            token = token.substring(7);

            // Analyze token to get username
            String username = jwtUtils.getUsernameFromToken(token);

            if (username == null) {
                return ResponseEntity.status(403).body(List.of());
            }

            User user = userService.getCurrentUser();

            Role role = user.getRoles().iterator().next();
            logger.info("Identified Role: {}", role.getName().name());

            List<CourseRequest> courseRequests;

            switch (role.getName()) {
                case ROLE_LECTURER -> {
                    List<Course> lecturerCourses = lecturerService.getCoursesByLecturer(user.getId());
                    courseRequests = lecturerCourses.stream()
                            .map(this::convertToRequest)
                            .collect(Collectors.toList());
                }
                case ROLE_STUDENT -> {
                    if (!(user instanceof Student student)) {
                        return ResponseEntity.status(403).body(List.of());
                    }
                    List<Course> studentCourses = studentService.getCoursesByStudentRegNumber(student.getRegNumber());
                    courseRequests = studentCourses.stream()
                            .map(this::convertToRequest)
                            .collect(Collectors.toList());
                }
                default -> {
                    return ResponseEntity.status(403).body(List.of());
                }
            }

            return ResponseEntity.ok(courseRequests);

        } catch (UserPrincipalNotFoundException e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    public ResponseEntity<List<StudentDTO>> getLecturerStudents(HttpServletRequest request) {
        try {
            // Extract and validate token
            String token = extractAndValidateToken(request);
            if (token == null) {
                return ResponseEntity.status(401).body(List.of());
            }
    
            // Get current user and validate role
            User currentUser = userService.getCurrentUser();
            if (!hasRole(currentUser, "ROLE_LECTURER")) {
                return ResponseEntity.status(403).body(List.of());
            }
    
            // Fetch lecturer and their students
            Lecturer lecturer = (Lecturer) currentUser;
            List<StudentDTO> studentDTOs = lecturer.getCourses().stream()
                .flatMap(course -> course.getStudents().stream())
                .map(mappingUtil::mapToStudentDTO)
                .collect(Collectors.toList());
    
            return ResponseEntity.ok(getUniqueStudentDTOList(studentDTOs));
        } catch (UserPrincipalNotFoundException e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    private List<StudentDTO> getUniqueStudentDTOList(List<StudentDTO> studentDTOs){
        Set<StudentDTO> uniqueStudents = new HashSet<>(studentDTOs);
        return new ArrayList<>(uniqueStudents);
    }
    
    private String extractAndValidateToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
    
        token = token.substring(7);
        String username = jwtUtils.getUsernameFromToken(token);
        return (username != null) ? token : null;
    }
    
    private boolean hasRole(User user, String roleName) {
        return user.getRoles().stream()
            .anyMatch(role -> role.getName().name().equals(roleName));
    }
    

    private CourseRequest convertToRequest(Course course) {
        CourseRequest requestDTO = new CourseRequest();

        requestDTO.setCourseCode(course.getCourseCode());
        requestDTO.setCourseTitle(course.getCourseTitle());

        return requestDTO;
    }

}
