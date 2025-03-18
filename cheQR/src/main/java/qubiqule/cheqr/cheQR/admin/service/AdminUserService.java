package qubiqule.cheqr.cheQR.admin.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.admin.AdminUser;
import qubiqule.cheqr.cheQR.admin.AdminUserRepository;
import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;
import qubiqule.cheqr.cheQR.admin.payload.response.DeletionResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.LecturerByCampusResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.LecturerCampusResponse;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Institution;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.repository.CampusRepository;
import qubiqule.cheqr.cheQR.repository.InstitutionRepository;
import qubiqule.cheqr.cheQR.repository.LecturerRepository;
import qubiqule.cheqr.cheQR.service.UserService;

@Service
@Slf4j
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MappingUtil mappingUtil;

    public List<AdminUser> getAllAdminUsers() {
        List<User> users = userService.getAllUsers();
        List<AdminUser> adminUsers = new ArrayList<>();
        for (User user : users) {
            if(user instanceof AdminUser){
                adminUsers.add((AdminUser)user);
            }
        }
        return adminUsers;
    }

    @Transactional
    public AdminUser verifyInstitutionAdmin(Long adminId) {
        User user = userService.findById(adminId)
                .orElseThrow(() -> new RuntimeException());
        AdminUser adminUser = new AdminUser();
            
        if(user instanceof AdminUser){
            user.setVerified(true);
            userService.saveUser(user);
        }
        adminUser = (AdminUser)user;
        System.out.println("Verified: " + adminUser.isVerified());
        return adminUserRepository.save(adminUser);
    }

    public ResponseEntity<InstitutionDTO> createInstitution(String name) throws UserPrincipalNotFoundException {

        User user = userService.getCurrentUser();
        AdminUser adminUser = new AdminUser();
        if(user instanceof AdminUser){
            adminUser = (AdminUser) user;
        }

        if (institutionRepository.findByInstitutionNameIgnoreCase(name).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Institution institution = new Institution();

        institution.setInstitutionName(name);
        institution.setAdminUser(adminUser);

        Institution updatedInstitution = institutionRepository.save(institution);

        adminUser.setInstitution(institution);
        adminUserRepository.save(adminUser);

        return ResponseEntity.ok(mappingUtil.mapToInstitutionDTO(updatedInstitution));
    }

    public ResponseEntity<?> updateInstitution(String name, Campus campus) {
        Optional<Institution> institutionOpt = institutionRepository.findByInstitutionNameIgnoreCase(name);

        if (institutionOpt.isPresent()) {

            Institution institution = institutionOpt.get();

            institution.getCampuses().add(campus);

            institutionRepository.save(institution);

            return ResponseEntity.ok("Institution updated successfully");

        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<InstitutionDTO> getAdminInstitution(Long adminId) {
        AdminUser adminUser = (AdminUser) userService.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found!"));

        InstitutionDTO institutionDTO = mappingUtil.mapToInstitutionDTO(adminUser.getInstitution());

        return ResponseEntity.ok(institutionDTO);
    }

    public ResponseEntity<LecturerByCampusResponse> getLecturersByCampus(@RequestBody String campusId) {
        Optional<Campus> campusOpt = campusRepository.findById(Long.parseLong(campusId));

        if (campusOpt.isEmpty()) {
            return ResponseEntity.ok().body(new LecturerByCampusResponse("Campus not found", null));
        } else {
            Campus campus = campusOpt.get();

            List<Lecturer> lecturers = campus.getLecturers();
            Set<LecturerDTO> lecturerDTOs = lecturers.stream().map((lect) -> mappingUtil.mapToLecturerDTO(lect))
                    .collect(Collectors.toSet());

            return ResponseEntity.ok()
                    .body(new LecturerByCampusResponse("Lecturers fetched successfully", lecturerDTOs));
        }
    }

    @Transactional
    public ResponseEntity<DeletionResponse> deleteInstitution(String institutionName) throws UserPrincipalNotFoundException {

        User user = userService.getCurrentUser();

        AdminUser adminUser = (AdminUser) user;

        Institution institution = institutionRepository.findByInstitutionNameIgnoreCase(institutionName)
                .orElseThrow(() -> new EntityNotFoundException("Institution not found!"));

        if (institution.getCampuses() != null) {
            for (Campus campus : institution.getCampuses()) {
                if (campus.getLecturers() != null) {
                    for (Lecturer lecturer : campus.getLecturers()) {
                        lecturer.setCampus(null);
                        lecturerRepository.save(lecturer);
                    }
                    campus.setLecturers(null);
                    campusRepository.save(campus);
                    campusRepository.delete(campus);
                }
            }
            institution.setCampuses(null);
            adminUser.setInstitution(null);
            adminUserRepository.save(adminUser);
            institution.setAdminUser(null);
            institutionRepository.save(institution);
            institutionRepository.delete(institution);
        } else {

            adminUser.setInstitution(null);
            adminUserRepository.save(adminUser);
            institution.setAdminUser(null);
            institutionRepository.save(institution);

            institutionRepository.delete(institution);
        }
        return ResponseEntity.ok(new DeletionResponse("Institution deleted successfully!"));
    }

    public ResponseEntity<LecturerCampusResponse> assignLecturerToInstitution(String lecturerUsername,
            String campusName) {
        Optional<Lecturer> lecturerOpt = lecturerRepository.findByUsername(lecturerUsername);
        Optional<Campus> campusOpt = campusRepository.findByCampusName(campusName);

        if (lecturerOpt.isEmpty() || campusOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LecturerCampusResponse("Lecturer or campus not found!", null, null));
        } else {
            Lecturer lecturer = lecturerOpt.get();
            Campus campus = campusOpt.get();

            campus.getLecturers().add(lecturer);

            lecturerRepository.save(lecturer);
            campusRepository.save(campus);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LecturerCampusResponse("Lecturer added to campus successfully!", lecturer, campus));
        }
    }

    public ResponseEntity<Set<LecturerDTO>> getAllLecturers() {
        Set<Lecturer> lecturers = lecturerRepository.findAll().stream().collect(Collectors.toSet());
        Set<LecturerDTO> lecturerDTOs = lecturers.stream().map((lect) -> mappingUtil.mapToLecturerDTO(lect))
                .collect(Collectors.toSet());

        return ResponseEntity.ok().body(lecturerDTOs);
    }

    public String getVerifiedStatus(Long id){
        User user = userService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found!"));

        return String.valueOf(user.isVerified());
    }
}
