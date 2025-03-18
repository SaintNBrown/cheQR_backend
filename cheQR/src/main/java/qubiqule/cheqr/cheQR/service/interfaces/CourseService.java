package qubiqule.cheqr.cheQR.service.interfaces;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Student;

public interface CourseService {
    Course createCourse(String courseCode, String courseTitle) throws UserPrincipalNotFoundException;
    Student addStudentToCourse(String courseCode, String regNumber);
    List<Student> getStudentsByCourse(Long courseId);
    List<Course> getCoursesByStudent(Long studentId);
    Course findByCourseCode(String courseCode);
    Course saveCourse(Course course);
}
