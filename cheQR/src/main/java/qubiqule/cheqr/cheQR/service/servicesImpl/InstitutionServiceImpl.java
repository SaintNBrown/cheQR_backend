package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Institution;
import qubiqule.cheqr.cheQR.repository.CampusRepository;
import qubiqule.cheqr.cheQR.repository.InstitutionRepository;
import qubiqule.cheqr.cheQR.service.interfaces.InstitutionService;

@Service
public class InstitutionServiceImpl implements InstitutionService {
    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private MappingUtil mappingUtil;

    @Override
    public List<Institution> getAllInstitutions(){
        return institutionRepository.findAll();
    }

    @Override
    public Institution getInstitutionById(Long Id){
        return institutionRepository.findById(Id)
            .orElseThrow(() -> new EntityNotFoundException("Institution not found!"));
    }

    @Override
    public Institution addInstitution(Institution institution) {
        return institutionRepository.save(institution);
    }

    @Override
    public List<Institution> getInstitutions() {
        return institutionRepository.findAll();
    }

    @Override
    public Campus addCampusToInstitution(Long institutionId, Campus campus) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new EntityNotFoundException("Institution not found"));
        campus.setInstitution(institution);
        return campusRepository.save(campus);
    }

    @Override
    public List<Campus> getCampuses(Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new EntityNotFoundException("Institution not found"));
        return institution.getCampuses();
    }

    @Override
    public Institution updateInstitution(String id, InstitutionDTO institutionDTO) {
        Institution institution = institutionRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new EntityNotFoundException("Institution not found!"));

        institutionDTO.getCampuses().forEach(campus -> {
            Campus campus_ = mappingUtil.mapToCampus(campus);
            institution.getCampuses().add(campus_);
        });

        institution.setInstitutionName(institutionDTO.getName());

        return institution;
    }

    public Map<String, Object> checkInstitutionExists(String name){
        Map<String, Object> response = new HashMap<>();
        Optional<Institution> institution = institutionRepository.findByInstitutionNameIgnoreCase(name);

        if(institution.isPresent()){
            response.put("response", "Sorry, institution already has an admin account.");
            return response;
        }else {
            response.put("response", null);
            return response;
        }

    }
}
