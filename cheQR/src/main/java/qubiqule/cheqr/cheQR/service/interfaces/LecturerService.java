package qubiqule.cheqr.cheQR.service.interfaces;

import java.util.List;

import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;

public interface LecturerService {
    List<Lecturer> getAllLecturers();
    Campus getLecturerCampus(Long id);
    Lecturer getLecturerById(Long Id);
    Course addCourseToLecturer(Long lecturerId, Course course);
    List<Course> getCoursesByLecturer(Long lecturerId);
    Lecturer addLecturer(Lecturer lecturer);
    Lecturer getLecturerByUsername(String username);
    Lecturer saveLecturer(Lecturer lecturer);
    void deleteLecturer(String username);
}
