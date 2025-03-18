package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;
import java.util.Map;

import qubiqule.cheqr.cheQR.admin.payload.response.GetCampusVenuesResponse;
import qubiqule.cheqr.cheQR.models.business.Venue;

public interface VenueService {
    Venue addVenue(Venue venue);

    List<Venue> getVenues();

    GetCampusVenuesResponse getVenuesByCampusId(Long campusId);

    Venue assignVenueToSession(Long venueId, Long sessionId);

    Map<String, Object> deleteVenue(String venueId);

    Venue getVenueFromId(Long venueId);
}
