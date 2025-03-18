package qubiqule.cheqr.cheQR.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import qubiqule.cheqr.cheQR.DTO.CampusInstitutionDTO;
import qubiqule.cheqr.cheQR.DTO.CourseDTO;
import qubiqule.cheqr.cheQR.DTO.StudentDTO;
import qubiqule.cheqr.cheQR.admin.payload.response.CampusResponse;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.payload.requests.CourseRequest;
import qubiqule.cheqr.cheQR.service.business.BusinessService;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;
import qubiqule.cheqr.cheQR.service.interfaces.StudentService;

@RestController
@RequestMapping("/api")
public class BusinessController {

    //private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private BusinessService businessService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MappingUtil mappingUtil;

    @GetMapping("/users/courses")
    public ResponseEntity<List<CourseRequest>> getUserCourses(HttpServletRequest request){
        return businessService.getUserCourses(request);
    }

    @GetMapping("/lecturer/students")
    public ResponseEntity<List<StudentDTO>> getLecturerStudents(HttpServletRequest request){
        return businessService.getLecturerStudents(request);
    }

    @GetMapping("/lecturer/getCampus")
    public ResponseEntity<CampusResponse> getLecturerCampus(@RequestParam String lecturerId) throws UserPrincipalNotFoundException{
        Campus campus = lecturerService.getLecturerCampus(Long.parseLong(lecturerId));
        return ResponseEntity.ok().body(new CampusResponse("Campus fetched successfully!", mappingUtil.mapToCampusDTO(campus)));
    }

    @GetMapping("/lecturer/institution")
    public ResponseEntity<CampusInstitutionDTO> getLecturerInstitutionName(@RequestParam String lecturerId) throws UserPrincipalNotFoundException{
        Campus campus = lecturerService.getLecturerCampus(Long.parseLong(lecturerId));
        return ResponseEntity.ok().body(mappingUtil.mapToCampusInstitutionDTO(campus));
    }

    @GetMapping("/student/courses")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(@RequestParam String studentId){
        return ResponseEntity.ok().body(mappingUtil.mapToCourseDTOs(studentService.getCoursesByStudent(Long.parseLong(studentId))));
    }
}
