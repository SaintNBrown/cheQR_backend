package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;
import java.util.Map;

import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusLecturerDTO;
import qubiqule.cheqr.cheQR.admin.payload.response.CampusResponse;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;

public interface CampusService {
    List<Campus> getAllCampuses();
    CampusResponse createCampus(CampusDTO campusDTO);
    Lecturer addLecturerToCampus(CampusLecturerDTO dto);
    void removeLecturerFromCampus(String username);
    List<Lecturer> getLecturers(Long campusId);
    Campus findCampusById(Long campusId);
    Map<String, Object> deleteCampus(String Id);
}
