package qubiqule.cheqr.cheQR.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import qubiqule.cheqr.cheQR.DTO.CourseDTO;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.payload.requests.CourseRequest;
import qubiqule.cheqr.cheQR.service.interfaces.CourseService;

@Transactional
@RestController
@RequestMapping("/api/lecturer")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private MappingUtil mappingUtil;

    @PostMapping("/createCourse")
    public ResponseEntity<CourseDTO> createCourse(
            @Valid @RequestBody CourseRequest courseRequest) throws UserPrincipalNotFoundException {
        logger.info("Received a request: " + courseRequest.getCourseTitle() + " " + courseRequest.getCourseCode());

        return ResponseEntity.ok(mappingUtil.mapToCourseDto(courseService.createCourse(courseRequest.getCourseCode(), courseRequest.getCourseTitle())));
    } 

    @PostMapping("/enroll")
    public ResponseEntity<Map<String, Object>> addStudentToCourse(@RequestParam String courseCode, @RequestParam String regNumber){
        Map<String, Object> response = new HashMap<>();
        Student student = courseService.addStudentToCourse(courseCode, regNumber);
        response.put("Student enrolled successfully!", mappingUtil.mapToStudentDTO(student));
        return ResponseEntity.ok(response);
    }

}
