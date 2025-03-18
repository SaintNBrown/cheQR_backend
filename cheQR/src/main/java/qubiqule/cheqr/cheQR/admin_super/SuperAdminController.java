package qubiqule.cheqr.cheQR.admin_super;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import qubiqule.cheqr.cheQR.DTO.AdminUserDTO;
import qubiqule.cheqr.cheQR.DTO.StudentDTO;
import qubiqule.cheqr.cheQR.DTO.UserDTO;
import qubiqule.cheqr.cheQR.admin.AdminUser;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;
import qubiqule.cheqr.cheQR.admin.service.AdminUserService;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.service.UserService;
import qubiqule.cheqr.cheQR.service.interfaces.CampusService;
import qubiqule.cheqr.cheQR.service.interfaces.InstitutionService;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;
import qubiqule.cheqr.cheQR.service.interfaces.StudentService;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private CampusService campusService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MappingUtil mappingUtil;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(mappingUtil.mapUserDTOs(users));
    }

    @GetMapping("/institution-admins")
    public ResponseEntity<List<AdminUserDTO>> getAllAdminUsers(){
        return ResponseEntity.ok(mappingUtil.mapAdminUserDTOs(adminUserService.getAllAdminUsers()));
    }

    @GetMapping("/verify-admin/{adminId}")
    public ResponseEntity<AdminUserDTO> verifyInstitutionAdmin(@PathVariable String adminId){
        AdminUser adminUser = adminUserService.verifyInstitutionAdmin(Long.parseLong(adminId));
        return ResponseEntity.ok().body(mappingUtil.mapToAdminUserDTO(adminUser));
    }

    @GetMapping("/institutions")
    public ResponseEntity<List<InstitutionDTO>> getInsitutions(){
        return ResponseEntity.ok().body(mappingUtil.mapTInstitutionDTOs(institutionService.getAllInstitutions()));
    }

    @GetMapping("/campuses")
    public ResponseEntity<List<CampusDTO>> getCampuses(){
        return ResponseEntity.ok().body(mappingUtil.mapToCampusDTOs(campusService.getAllCampuses()));
    }


    @GetMapping("/lecturers")
    public ResponseEntity<List<LecturerDTO>> getLecturers(){
        return ResponseEntity.ok().body(mappingUtil.mapToLecturerDTOs(lecturerService.getAllLecturers()));
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDTO>> getStudents(){
        return ResponseEntity.ok().body(mappingUtil.mapToStudentDTOs(studentService.getAllStudents()));
    }
}
