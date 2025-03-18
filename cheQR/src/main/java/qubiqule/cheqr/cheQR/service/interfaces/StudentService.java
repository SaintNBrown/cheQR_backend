package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;

import qubiqule.cheqr.cheQR.DTO.AttendanceRecordsDTO;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Student;

public interface StudentService {
    Student addStudent(Student student);

    List<Student> getAllStudents();

    List<Course> getCoursesByStudent(Long studentId);

    Student registerStudentForCourse(String regNumber, Long courseId);

    Student getStudentByRegNumber(String regNumber);

    List<Course> getCoursesByStudentRegNumber(String regNumber);

    List<AttendanceRecordsDTO> fetchStudentAttendanceRecord(String regNumber, String courseCode);

    void deleteStudent(String regNumber);
}
