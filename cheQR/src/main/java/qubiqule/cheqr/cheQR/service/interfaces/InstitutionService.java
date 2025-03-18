package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;
import java.util.Map;

import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Institution;

public interface InstitutionService {
    List<Institution> getAllInstitutions();
    Institution addInstitution(Institution institution);
    Institution getInstitutionById(Long Id);
    List<Institution> getInstitutions();
    Campus addCampusToInstitution(Long institutionId, Campus campus);
    List<Campus> getCampuses(Long institutionId);
    Institution updateInstitution(String id, InstitutionDTO institutionDTO);
    Map<String, Object> checkInstitutionExists(String name);

}
