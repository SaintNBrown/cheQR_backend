package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusLecturerDTO;
import qubiqule.cheqr.cheQR.admin.payload.response.CampusResponse;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Institution;
import qubiqule.cheqr.cheQR.models.business.Venue;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.repository.CampusRepository;
import qubiqule.cheqr.cheQR.repository.InstitutionRepository;
import qubiqule.cheqr.cheQR.repository.VenueRepository;
import qubiqule.cheqr.cheQR.service.interfaces.CampusService;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;

@Service
public class CampusServiceImpl implements CampusService {
    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private MappingUtil mappingUtil;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private LecturerService lecturerService;

    @Override
    public List<Campus> getAllCampuses(){
        return campusRepository.findAll();
    }

    @Override
    public Lecturer addLecturerToCampus(CampusLecturerDTO dto) {
        Campus campus = campusRepository.findById(Long.parseLong(dto.getCampusId()))
                .orElseThrow(() -> new EntityNotFoundException("Campus not found"));

        Lecturer lecturer = lecturerService.getLecturerByUsername(dto.getLecturerUsername());

        lecturer.setCampus(campus);

        if (campus.getLecturers() == null) {
            campus.setLecturers(new ArrayList<>());
        }
        campus.getLecturers().add(lecturer);

        Lecturer savedLecturer = lecturerService.saveLecturer(lecturer);
        campusRepository.save(campus);

        return savedLecturer;
    }

    @Override
    public void removeLecturerFromCampus(String username) {
        Lecturer lecturer = lecturerService.getLecturerByUsername(username);
        Campus campus = !(lecturer.getCampus() == null) ? lecturer.getCampus() : null;

        if (!(campus == null)) {
            lecturer.setCampus(null);
            campus.getLecturers().remove(lecturer);
            campusRepository.save(campus);
            lecturerService.saveLecturer(lecturer);
        }
    }

    @Override
    public List<Lecturer> getLecturers(Long campusId) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(() -> new EntityNotFoundException("Campus not found"));
        return campus.getLecturers();
    }

    @Override
    public Campus findCampusById(Long campusId) {
        return campusRepository.findById(campusId).orElseThrow(() -> new EntityNotFoundException("Campus not found!"));
    }

    @Override
    public CampusResponse createCampus(CampusDTO campusDTO) {
        // Find institution
        Institution institution = institutionRepository.findById(Long.parseLong(campusDTO.getInstitutionId()))
                .orElseThrow(() -> new EntityNotFoundException("Institution not found!"));

        // Check for existing campus
        if (campusRepository.findByCampusNameAndInstitution(campusDTO.getCampusName(), institution)
                .isPresent()) {
            return new CampusResponse("Campus already exists for this institution.", null);
        }

        // Create and save new campus
        Campus campus = new Campus(null, campusDTO.getCampusName(), institution, null, null);
        institution.getCampuses().add(campus);
        Campus savedCampus = campusRepository.save(campus);
        institutionRepository.saveAndFlush(institution);

        return new CampusResponse("Campus created successfully", mappingUtil.mapToCampusDTO(savedCampus));
    }

    @Transactional
    @Override
    public Map<String, Object> deleteCampus(String Id) {
        Map<String, Object> response = new HashMap<>();

        Optional<Campus> campusOpt = campusRepository.findById(Long.parseLong(Id));

        if (campusOpt.isEmpty()) {
            response.put("error", "Campus not found!");
            return response;
        }

        Campus campus = campusOpt.get();
        for (Lecturer lecturer : campus.getLecturers()) {
            lecturer.setCampus(null);
            lecturerService.saveLecturer(lecturer);
        }

        if (campus.getLecturers() == null) {
            campus.setLecturers(new ArrayList<>());
        }

        campus.getLecturers().clear();

        for (Venue venue : campus.getVenues()) {
            venue.setCampus(null);
            venueRepository.save(venue);
        }

        if (campus.getVenues() == null) {
            campus.setVenues(new ArrayList<>());
        }

        campus.getVenues().clear();

        campus.setInstitution(null);

        campusRepository.delete(campus);

        response.put("success", "Campus deleted successfully");
        return response;
    }
}
