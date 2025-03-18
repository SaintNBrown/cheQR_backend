package qubiqule.cheqr.cheQR.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qubiqule.cheqr.cheQR.DTO.AttendanceListDTO;
import qubiqule.cheqr.cheQR.DTO.SessionDTO;
import qubiqule.cheqr.cheQR.entity.service.QrCodeService;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.payload.requests.AttendanceData;
import qubiqule.cheqr.cheQR.service.interfaces.AttendanceService;
import qubiqule.cheqr.cheQR.service.interfaces.ClassSessionService;

@RestController
@RequestMapping("/api")
public class ClassSessionController {
    @Autowired
    private ClassSessionService classSessionService;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private MappingUtil mappingUtil;

    @GetMapping("/lecturer/{sessionId}/qr-code-string")
    public ResponseEntity<String> fetchSessionQrCode(@PathVariable String sessionId) {
        ClassSession classSession = classSessionService.getClassSession(Long.parseLong(sessionId));

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(classSession.getQrCodeString());
    }

    @PostMapping("/student/attendance")
    public ResponseEntity<Map<String, Object>> markAttendance(@RequestBody AttendanceData attendanceData) {
        return qrCodeService.recordAttendance(attendanceData);
    }

    @GetMapping("/lecturer/currentSessions")
    public ResponseEntity<List<SessionDTO>> getCurrentSessions(@RequestParam String userId)
            throws UserPrincipalNotFoundException {
        List<SessionDTO> sessionDTOs = mappingUtil.mapToSessionDTOs(classSessionService.getCurrentClassSessions());
        return ResponseEntity.ok(sessionDTOs);
    }

    @GetMapping("/lecturer/getAttendanceList")
    public ResponseEntity<AttendanceListDTO> getAttendanceListBySession(@RequestParam String sessionId) {
        AttendanceListDTO attendanceListDTO = mappingUtil
                .mapToAttendanceListDTO(attendanceService.getAttendanceListBySession(Long.parseLong(sessionId)));
        return ResponseEntity.ok(attendanceListDTO);
    }

    @GetMapping("/lecturer/getSessionDetails")
    public ResponseEntity<SessionDTO> getSessionById(@RequestParam String sessionId) {
        SessionDTO sessionDTO = mappingUtil
                .mapToSessionDTO(classSessionService.getClassSession(Long.parseLong(sessionId)));
        return ResponseEntity.ok(sessionDTO);
    }

    @DeleteMapping("/lecturer/deleteSession")
    public ResponseEntity<Map<String, Object>> deleteSchedule(@RequestParam String sessionId) {
        Map<String, Object> response = new HashMap<>();
        classSessionService.deleteClassSession(sessionId);
        response.put("response", "Class session deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
