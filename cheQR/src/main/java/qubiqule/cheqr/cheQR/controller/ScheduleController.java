package qubiqule.cheqr.cheQR.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.DTO.schedule.ScheduleCreationRequest;
import qubiqule.cheqr.cheQR.DTO.schedule.ScheduleDTO;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.Schedule;
import qubiqule.cheqr.cheQR.service.interfaces.ScheduleService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/lecturer")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MappingUtil mappingUtil;

    @PostMapping("/schedules")
    public ResponseEntity<ScheduleDTO> createSchedule(@RequestBody ScheduleCreationRequest scheduleCreationRequest) {
        log.info("Lecturer Id: {}", scheduleCreationRequest);
        Schedule schedule = scheduleService.createSchedule(Long.parseLong(scheduleCreationRequest.getLecturerId()),
                scheduleCreationRequest.getScheduleRequest());
        return ResponseEntity.status(HttpStatus.CREATED).body(mappingUtil.mapToScheduleDTO(schedule));
    }

    @GetMapping("/schedules")
    public List<ScheduleDTO> getLecturerSchedules(@RequestParam String lecturerId) {
        return mappingUtil.mapToScheduleDTOs(scheduleService.getLecturerSchedules(Long.parseLong(lecturerId)));
    }

    @DeleteMapping("/schedules")
    public ResponseEntity<Map<String, Object>> deleteSchedule(@RequestParam String scheduleId) {
        Map<String, Object> response = new HashMap<>();
        scheduleService.deleteSchedule(Long.parseLong(scheduleId));
        response.put("response", "Schedule deleted successfully!");
        return ResponseEntity.ok(response);

    }

}
