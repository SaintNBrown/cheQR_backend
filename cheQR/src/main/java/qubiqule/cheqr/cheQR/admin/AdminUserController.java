package qubiqule.cheqr.cheQR.admin;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusLecturerDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.VenueDTO;
import qubiqule.cheqr.cheQR.admin.payload.response.CampusByInstitutionResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.CampusResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.DeletionResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.GetCampusVenuesResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.InstitutionUpdateResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.LecturerByCampusResponse;
import qubiqule.cheqr.cheQR.admin.payload.response.VenueResponse;
import qubiqule.cheqr.cheQR.admin.service.AdminUserService;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Institution;
import qubiqule.cheqr.cheQR.models.business.Venue;
import qubiqule.cheqr.cheQR.service.UserService;
import qubiqule.cheqr.cheQR.service.interfaces.CampusService;
import qubiqule.cheqr.cheQR.service.interfaces.InstitutionService;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;
import qubiqule.cheqr.cheQR.service.interfaces.VenueService;

@RestController
@RequestMapping("/api")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private VenueService venueService;

    @Autowired
    private CampusService campusService;

    @Autowired
    private InstitutionService institutionService;

    @Autowired
    private UserService userService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private MappingUtil mappingUtil;

    //admin methods
    @GetMapping("/auth/admin/userExists")
    public ResponseEntity<Boolean> userExists(@RequestParam String username){
        return this.userService.userExists(username);
    }

    /*
     * Insitution methods
     */

    @GetMapping("/admin/getAdminInstitution")
    public ResponseEntity<InstitutionDTO> getInstitutions(@RequestParam String adminId) {
        return adminUserService.getAdminInstitution(Long.parseLong(adminId));
    }

    @PutMapping("/admin/updateInstitution/{id}")
    public ResponseEntity<InstitutionUpdateResponse> updateInstitution(@PathVariable String id,
            @RequestBody InstitutionDTO institutionDTO) {
        Institution updatedInstitution = institutionService.updateInstitution(id, institutionDTO);

        return ResponseEntity.ok(new InstitutionUpdateResponse("Successful!", mappingUtil.mapToInstitutionDTO(updatedInstitution)));
    }

    @GetMapping("/admin/getInstitutionById")
    public ResponseEntity<Map<String, Object>> getInstitutionById(@RequestParam String id) {
        Map<String, Object> response = new HashMap<>();
        Institution institution = institutionService.getInstitutionById(Long.parseLong(id));
        response.put("response", institution);
        return ResponseEntity.ok(response);

    }

    /**
     * 
     * @param name
     * @return Institution
     */
    @PostMapping("/admin/createInstitution")
    public ResponseEntity<InstitutionDTO> createInstitution(@RequestBody String name) throws Exception {
        return adminUserService.createInstitution(name);
    }

    @GetMapping("/admin/checkNameExists")
    public Map<String, Object> checkInstitutionExists(@RequestParam String name){
        return institutionService.checkInstitutionExists(name);
    }

    /**
     * 
     * @param institutionName
     * @return
     * @throws UserPrincipalNotFoundException
     */
    @DeleteMapping("/admin/deleteInstitution")
    public ResponseEntity<DeletionResponse> deleteInsitution(@RequestParam String institutionName)
            throws UserPrincipalNotFoundException {
        return this.adminUserService.deleteInstitution(institutionName);
    }

    /*
     * Campus methods
     */

     /**
      * 
      * @param campusDTO
      * @return
      * @throws JsonMappingException
      * @throws JsonProcessingException
      */
    @PostMapping("/admin/createCampus")
    public ResponseEntity<CampusResponse> createCampus(@RequestBody CampusDTO campusDTO)
            throws JsonMappingException, JsonProcessingException {
        return ResponseEntity.ok(this.campusService.createCampus(campusDTO));
    }

    /**
     * 
     * @param username
     */
    @DeleteMapping("/admin/removeLecturerFromCampus")
    public void removeLecturerFromCampus(@RequestParam String username){
        campusService.removeLecturerFromCampus(username);
    }

    /**
     * 
     * @param institutionId
     * @return
     */
    @GetMapping("/admin/getCampusesByInstitution")
    public ResponseEntity<CampusByInstitutionResponse> getCampusesByInstitution(@RequestParam String institutionId) {
        List<Campus> campuses = institutionService.getCampuses(Long.parseLong(institutionId));
        List<CampusDTO> campusDTOs = new LinkedList<>();
        campuses.forEach((campus) -> {
            CampusDTO campus_ = mappingUtil.mapToCampusDTO(campus);
            campusDTOs.add(campus_);
        });
        return ResponseEntity.ok(new CampusByInstitutionResponse("Successful!", campusDTOs));
    }

    /**
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/admin/deleteCampus")
    public ResponseEntity<Map<String, Object>> deleteCampus(@RequestParam String id) {
        return ResponseEntity.ok(campusService.deleteCampus(id));
    }

    /*
     * Venue methods
     */

     /**
      * 
      * @param venueDTO
      * @return
      */
    @PostMapping("/admin/createVenue")
    public ResponseEntity<VenueResponse> createVenue(@RequestBody VenueDTO venueDTO) {
        Venue venue = mappingUtil.mapToVenue(venueDTO);
        
        return ResponseEntity.ok(new VenueResponse("Venue creatd successfully!", 
        mappingUtil.mapToVenueDTO(this.venueService.addVenue(venue))));
    }

    @GetMapping("/admin/getVenuesByCampusId")
    public ResponseEntity<GetCampusVenuesResponse> getVenuesByCampusId(@RequestParam String campusId){
        return ResponseEntity.ok(this.venueService.getVenuesByCampusId(Long.parseLong(campusId)));
    }

    @DeleteMapping("/admin/deleteVenue")
    public ResponseEntity<Map<String, Object>> deleteVenue(@RequestParam String venueId) {
        Map<String, Object> response = new HashMap<>();
        response.put("response", this.venueService.deleteVenue(venueId));
        return ResponseEntity.ok(response);
    }

    /*
     * Lecturer methods
     */
    @DeleteMapping("/admin/deleteLecturer")
    public void deleteLecturer(@RequestParam String lecturerUsername) {
        lecturerService.deleteLecturer(lecturerUsername);
    }

    @GetMapping("/admin/getAllLecturers")
    public ResponseEntity<Set<LecturerDTO>> getAllLecturers(){
        return adminUserService.getAllLecturers();
    }

    @GetMapping("/admin/getLecturersByCampus")
    public ResponseEntity<LecturerByCampusResponse> getLecturersByCampus(@RequestParam String campusId){
        return adminUserService.getLecturersByCampus(campusId);
    }

    @PostMapping("/admin/addLecturerToCampus")
    public ResponseEntity<Map<String, Object>> addLecturerToCampus(@RequestBody CampusLecturerDTO campusLecturerDTO) {
        Map<String, Object> response = new HashMap<>();
        response.put("response", mappingUtil.mapToLecturerDTO(campusService.addLecturerToCampus(campusLecturerDTO)));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/isVerified")
    public ResponseEntity<String> getVerifiedStatus(@RequestParam String id){
        return ResponseEntity.ok().body(adminUserService.getVerifiedStatus(Long.parseLong(id)));
    }
}
