package qubiqule.cheqr.cheQR.entity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;
import qubiqule.cheqr.cheQR.models.business.AttendanceStatus;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.business.GpsCordinates;
import qubiqule.cheqr.cheQR.models.business.Venue;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.payload.requests.AttendanceData;
import qubiqule.cheqr.cheQR.repository.AttendanceListRepository;
import qubiqule.cheqr.cheQR.repository.AttendanceRecordRepository;
import qubiqule.cheqr.cheQR.repository.ClassSessionRepository;
import qubiqule.cheqr.cheQR.repository.StudentRepository;

@Service
public class QrCodeService {
    private static final Logger logger = LoggerFactory.getLogger(QrCodeService.class);

    //private static final int EARTH_RADIUS_METERS = 6371 * 1000;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    @Autowired
    private AttendanceListRepository attendanceListRepository;

    @Autowired
    private StudentRepository studentRepository;

    public ResponseEntity<Map<String, Object>> recordAttendance(AttendanceData attendanceData) {

        System.out.println(attendanceData.toString());

        Map<String, Object> response = new HashMap<>();

        try {

            // decode the Qr code string
            String qrCode = attendanceData.getQrCodeString();

            // extract the session id
            Long extractedSessionId = extractSessionId(qrCode);

            System.out.println("Session id: " + extractedSessionId);

            Student student = studentRepository.findById(Long.parseLong(attendanceData.getStudentId()))
                    .orElseThrow(() -> new EntityNotFoundException("Student not found!"));

            // check optional session value is present
            ClassSession classSession = classSessionRepository.findById((extractedSessionId))
                    .orElseThrow(() -> new EntityNotFoundException("Class session not found!"));

            if (student.getCourses() == null || classSession.getCourse() == null ||
                    classSession.getCourse().getStudents() == null
                    || !classSession.getCourse().getStudents().contains(student)) {
                response.put("error", "You are not enrolled in the course!");
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
            }

            // check for proximity within class venue
            if (!isPointInsideVenue(
                classSession.getVenue(), 
                Double.parseDouble(attendanceData.getStudentLatitude()), 
                Double.parseDouble(attendanceData.getStudentLongitude()), 
                Double.parseDouble(attendanceData.getStudentAltitude()))) {

                response.put("error", "You are outside the class venue!");
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body(response);
            }

            saveAttendance(classSession, student);

            response.put("response", "Attendance marked successfully!");
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {

            logger.info("Error: {}", e.getMessage());
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {

            logger.info("Error: {}", e.getLocalizedMessage());
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        }
    }

    public Long extractSessionId(String qrCodeString) {
        String[] parts = qrCodeString.split(";");
        if (parts.length < 1) {
            throw new IllegalArgumentException("Invalid QR code format");
        }

        String sessionIdPart = parts[0];
        String sessionId = sessionIdPart.split(":")[1].trim();
        return Long.parseLong(sessionId);
    }


    /**
     * checks if a given cordinate (longitude, latitude, altitude ) is within a class venue
     * 
     * @param venue
     * @param latitude
     * @param longitude
     * @param altitude
     * @return
     */
    public boolean isPointInsideVenue(Venue venue, double latitude, double longitude, double altitude) {
        // Fast check: verify 2D position first (most cases fail here)
        if (!isPointInsidePolygon(venue, latitude, longitude)) {
            return false;
        }
    
        // Only interpolate altitude if point is within 2D footprint
        double interpolatedAltitude = interpolateAltitude(venue, latitude, longitude);
    
        //Tolerance to accommodate significant GPS altitude variations
        double altitudeTolerance = 500.0;
        
        // Check if point's altitude is within acceptable range
        return Math.abs(altitude - interpolatedAltitude) <= altitudeTolerance;
    }
    

    /**
     * Checks whether a point (longitude or latitude) lies 
     * within the 2D polygon formed by the venue's corners
     * 
     * @param venue
     * @param longitude
     * @param latitude
     * @return
     */
    private boolean isPointInsidePolygon(Venue venue, double latitude, double longitude) {
        // Get corners in correct order
        List<GpsCordinates> corners = List.of(
            venue.getFirstCornerCordinates(),
            venue.getSecondCornerCordinates(),
            venue.getThirdCornerCordinates(),
            venue.getFourthCornerCordinates()
        );
    
        // Quick bounding box check to early-exit for clearly outside points
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;
        
        for (GpsCordinates corner : corners) {
            minLat = Math.min(minLat, corner.getLatitude());
            maxLat = Math.max(maxLat, corner.getLatitude());
            minLon = Math.min(minLon, corner.getLongitude());
            maxLon = Math.max(maxLon, corner.getLongitude());
        }
        
        // Significantly increased buffer for handling extreme GPS inaccuracy
        // 0.0010 degrees is approximately 100-110 meters at the equator
        double buffer = 0.0010;
        
        // For extreme edge cases - if point is very close to bounding box, consider it inside
        // This helps reduce false negatives from GPS inaccuracy
        if (latitude < minLat - buffer || latitude > maxLat + buffer || 
            longitude < minLon - buffer || longitude > maxLon + buffer) {
            return false;
        }
        
        // Edge case: If point is very close to the bounding box edge (within 5 meters),
        // consider it inside to accommodate GPS jitter
        double innerBuffer = 0.00005; // ~5 meters
        if ((Math.abs(latitude - minLat) <= innerBuffer || Math.abs(latitude - maxLat) <= innerBuffer) ||
            (Math.abs(longitude - minLon) <= innerBuffer || Math.abs(longitude - maxLon) <= innerBuffer)) {
            return true;
        }
        
        // Continue with ray-casting algorithm for points not in the edge cases
        int n = corners.size();
        boolean result = false;
    
        for (int i = 0, j = n - 1; i < n; j = i++) {
            GpsCordinates cornerI = corners.get(i);
            GpsCordinates cornerJ = corners.get(j);
            
            // Check if horizontal ray from point crosses this edge
            if ((cornerI.getLatitude() > latitude) != (cornerJ.getLatitude() > latitude)) {
                // Avoid division by zero with an epsilon check
                double diffLat = cornerJ.getLatitude() - cornerI.getLatitude();
                if (Math.abs(diffLat) > 1e-10) {
                    double intersectLon = (cornerJ.getLongitude() - cornerI.getLongitude()) * 
                                         (latitude - cornerI.getLatitude()) / diffLat + 
                                         cornerI.getLongitude();
                    
                    if (longitude < intersectLon) {
                        result = !result;
                    }
                }
            }
        }
    
        return result;
    }

    /**
     * Interpolates the expected altitude at the given latitude and 
     * longitude based on the venue's corners.
     * 
     * @param venue
     * @param latitude
     * @param longitude
     * @return The interpolated altitude
     */
    private double interpolateAltitude(Venue venue, double latitude, double longitude) {
        GpsCordinates topLeft = venue.getFirstCornerCordinates();
        GpsCordinates topRight = venue.getSecondCornerCordinates();
        GpsCordinates bottomLeft = venue.getThirdCornerCordinates();
        GpsCordinates bottomRight = venue.getFourthCornerCordinates();
    
        // Handle degenerate cases more robustly
        // If venue dimensions are extremely small, just return the average altitude
        double latRange = Math.abs(bottomLeft.getLatitude() - topLeft.getLatitude());
        double lonRange = Math.abs(topRight.getLongitude() - topLeft.getLongitude());
    
        if (Math.max(latRange, lonRange) < 0.00001) { // Very small venue (< ~1 meter)
            return (topLeft.getAltitude() + topRight.getAltitude() + 
                    bottomLeft.getAltitude() + bottomRight.getAltitude()) / 4.0;
        }
    
        // Apply smoothing for small venues to reduce impact of corner altitude errors
        if (Math.max(latRange, lonRange) < 0.0001) { // Small venue (< ~10 meters)
            return (topLeft.getAltitude() + topRight.getAltitude() + 
                    bottomLeft.getAltitude() + bottomRight.getAltitude()) / 4.0;
        }

        // Calculate relative position (0-1 range)
        // Handle possible inverted coordinates
        double xRatio, yRatio;
        
        if (Math.abs(latRange) < 1e-10) {
            xRatio = 0.5; // Default to middle if the venue is flat in latitude
        } else {
            xRatio = (latitude - Math.min(topLeft.getLatitude(), bottomLeft.getLatitude())) / 
                     Math.abs(latRange);
        }
        
        if (Math.abs(lonRange) < 1e-10) {
            yRatio = 0.5; // Default to middle if the venue is flat in longitude
        } else {
            yRatio = (longitude - Math.min(topLeft.getLongitude(), topRight.getLongitude())) / 
                     Math.abs(lonRange);
        }
        
        // Constrain values to handle GPS inaccuracies at edges
        xRatio = Math.max(0.0, Math.min(1.0, xRatio));
        yRatio = Math.max(0.0, Math.min(1.0, yRatio));
    
        // Bilinear interpolation
        double top = topLeft.getAltitude() * (1 - yRatio) + topRight.getAltitude() * yRatio;
        double bottom = bottomLeft.getAltitude() * (1 - yRatio) + bottomRight.getAltitude() * yRatio;
    
        return top * (1 - xRatio) + bottom * xRatio;
    }

    private void saveAttendance(ClassSession classSession, Student student) {
        Optional<AttendanceList> attendanceList = attendanceListRepository.findByClassSession(classSession);

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setStudent(student);
        attendanceRecord.setAttendanceStatus(AttendanceStatus.PRESENT);
        attendanceRecordRepository.save(attendanceRecord);

        if (attendanceList.isEmpty()) {
            AttendanceList newList = new AttendanceList();
            newList.setClassSession(classSession);
            newList.getAttendanceRecords().add(attendanceRecord);

            attendanceListRepository.save(newList);

            attendanceRecord.setAttendanceList(newList);
            attendanceRecordRepository.save(attendanceRecord);
        } else {
            AttendanceList list = attendanceList.get();
            list.getAttendanceRecords().add(attendanceRecord);
            attendanceListRepository.save(list);

            attendanceRecord.setAttendanceList(list);
            attendanceRecordRepository.save(attendanceRecord);
        }
    }
}

// public boolean isWithinBoundingBox(double lat, double lon, ClassSession
// session) {
// double latMin = session.getVenue().getLatitude() -
// session.getVenue().getGeofenceRadius() / 111000;
// double latMax = session.getVenue().getLatitude() +
// session.getVenue().getGeofenceRadius() / 111000;
// double lonMin = session.getVenue().getLongitude() -
// session.getVenue().getGeofenceRadius()
// / (111000 * Math.cos(Math.toRadians(session.getVenue().getLatitude())));
// double lonMax = session.getVenue().getLongitude() +
// session.getVenue().getGeofenceRadius()
// / (111000 * Math.cos(Math.toRadians(session.getVenue().getLatitude())));

// return lat >= latMin && lat <= latMax && lon >= lonMin && lon <= lonMax;
// }

// public boolean isWithinGeofence(AttendanceData attendanceData, ClassSession classSession) {
    //     try {
    //         double studentLatitude = Double.parseDouble(attendanceData.getStudentLatitude());
    //         double studentLongitude = Double.parseDouble(attendanceData.getStudentLongitude());
    //         double venueLatitude = classSession.getVenue().getLatitude();
    //         double venueLongitude = classSession.getVenue().getLongitude();
    //         double geofenceRadius = classSession.getVenue().getGeofenceRadius();

    //         // Validate latitude and longitude values
    //         if (!isValidLatitude(studentLatitude) || !isValidLongitude(studentLongitude) ||
    //             !isValidLatitude(venueLatitude) || !isValidLongitude(venueLongitude)) {
    //             logger.warn("Invalid latitude or longitude values.");
    //             return false;
    //         }

    //         double distance = haversine(studentLatitude, studentLongitude, venueLatitude, venueLongitude);
    //         return distance <= geofenceRadius;
    //     } catch (NumberFormatException e) {
    //         logger.warn("Failed to parse latitude or longitude: " + e.getMessage());
    //         return false;
    //     }
    // }


    // /**
    //  * @param lat1
    //  * @param lon1
    //  * @param lat2
    //  * @param lon2
    //  * @return
    //  */
    // // Haversine formula to calculate distance between two lat/long points
    // private double haversine(double lat1, double lon1, double lat2, double lon2) {
    //     double latDistance = Math.toRadians(lat2 - lat1);
    //     double lonDistance = Math.toRadians(lon2 - lon1);

    //     double a = Math.pow(Math.sin(latDistance / 2), 2)
    //             + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
    //             * Math.pow(Math.sin(lonDistance / 2), 2);

    //     double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    //     return EARTH_RADIUS_METERS * c; // Distance in meters
    // }