package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.admin.payload.request.VenueDTO;
import qubiqule.cheqr.cheQR.admin.payload.response.GetCampusVenuesResponse;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.business.Venue;
import qubiqule.cheqr.cheQR.repository.ClassSessionRepository;
import qubiqule.cheqr.cheQR.repository.VenueRepository;
import qubiqule.cheqr.cheQR.service.interfaces.CampusService;
import qubiqule.cheqr.cheQR.service.interfaces.VenueService;

@Service
public class VenueServiceImpl implements VenueService {
    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private CampusService campusService;

    @Autowired
    private MappingUtil mappingUtil;

    @Override
    public Venue getVenueFromId(Long venueId){
        Venue venue = venueRepository.findById(venueId)
            .orElseThrow(() -> new EntityNotFoundException("Venue not found"));
            return venue;
    }

    @Override
    public Venue addVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    @Override
    public List<Venue> getVenues() {
        return venueRepository.findAll();
    }

    @Override
    public GetCampusVenuesResponse getVenuesByCampusId(Long campusId) {
        Campus campus = campusService.findCampusById(campusId);
        List<Venue> venues = campus.getVenues();

        Set<VenueDTO> venueDTOs = !(venues == null)
                ? venues.stream().map((venue) -> mappingUtil.mapToVenueDTO(venue)).collect(Collectors.toSet())
                : null;

        return new GetCampusVenuesResponse("Venues retrieved successfully!", venueDTOs);
    };

    @Override
    public Venue assignVenueToSession(Long venueId, Long sessionId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found"));
        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Class Session not found"));
        session.setVenue(venue);
        classSessionRepository.save(session);
        return venue;
    }

    @Override
    @Transactional
    public Map<String, Object> deleteVenue(String venueId) {
        Map<String, Object> response = new HashMap<>();
        Venue venue = venueRepository.findById(Long.parseLong(venueId))
                .orElseThrow(() -> new EntityNotFoundException("Venue not found!"));

        venue.setCampus(null);
        venueRepository.save(venue);
        venueRepository.delete(venue);

        response.put("response", "Venue deleted successfully!");
        return response;
    }

}
