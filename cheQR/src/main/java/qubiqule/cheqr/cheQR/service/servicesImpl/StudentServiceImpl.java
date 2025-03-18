package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.DTO.AttendanceRecordsDTO;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.repository.CourseRepository;
import qubiqule.cheqr.cheQR.repository.StudentRepository;
import qubiqule.cheqr.cheQR.service.interfaces.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MappingUtil mappingUtil;

    @Override
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Course> getCoursesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return student.getCourses();
    }

    @Override
    public Student registerStudentForCourse(String regNumber, Long courseId) {
        Student student = getStudentByRegNumber(regNumber);
        Course course = courseRepository.findById(courseId.toString())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.getStudents().add(student);
        student.getCourses().add(course);
        courseRepository.save(course);
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentByRegNumber(String regNumber) {
        return studentRepository.findByRegNumber(regNumber)
                .orElseThrow(() -> new EntityNotFoundException("Student not found!"));
    }

    // Get a student's courses
    @Override
    public List<Course> getCoursesByStudentRegNumber(String regNumber) {
        Student student = getStudentByRegNumber(regNumber);

        List<Course> courses = student.getCourses();

        return courses;
    }

    @Override
    public List<AttendanceRecordsDTO> fetchStudentAttendanceRecord(String regNumber, String courseCode) {
        Student student = getStudentByRegNumber(regNumber);
        List<Course> courses = getCoursesByStudent(student.getId());
        List<AttendanceRecord> attendanceRecord = student.getAttendanceRecords();

        List<AttendanceRecord> specificCourseAttendanceRecord = new ArrayList<>();

        if (!attendanceRecord.isEmpty()) {
            for (AttendanceRecord attendanceRecord_ : attendanceRecord) {
                if (courses.contains(attendanceRecord_.getAttendanceList().getClassSession().getCourse())) {
                    specificCourseAttendanceRecord.add(attendanceRecord_);
                }
            }
        }
        return mappingUtil.toRecordDTOs(specificCourseAttendanceRecord);
    }

    @Override
    @Transactional
    public void deleteStudent(String regNumber) {
        if (studentRepository.existsByRegNumber(regNumber)) {
            studentRepository.delete(studentRepository.findByRegNumber(regNumber).get());
        } else {
            throw new RuntimeException("Student not found with regNumber: " + regNumber);
        }
    }
}
