package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.repository.CourseRepository;
import qubiqule.cheqr.cheQR.repository.LecturerRepository;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;

@Service
public class LecturerServiceImpl implements LecturerService{
    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    public List<Lecturer> getAllLecturers(){
        return lecturerRepository.findAll();
    }

    @Override
    public Course addCourseToLecturer(Long lecturerId, Course course) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Lecturer not found"));
        course.setLecturer(lecturer);
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getCoursesByLecturer(Long lecturerId) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException("Lecturer not found"));
        return lecturer.getCourses();
    }

    @Override
    @Transactional
    public Lecturer addLecturer(Lecturer lecturer) {
        if (lecturerRepository.existsByUsername(lecturer.getUsername())) {
            throw new RuntimeException("Lecturer already exists with username: " + lecturer.getUsername());
        }
        return lecturerRepository.save(lecturer);
    }

    @Override
    public Lecturer getLecturerByUsername(String username){
        return lecturerRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Lecturer not found!"));
    }

    @Override
    public Lecturer getLecturerById(Long Id){
        return lecturerRepository.findById(Id).orElseThrow(() -> new EntityNotFoundException("Lecturer not found!"));
    }

    @Override
    @Transactional
    public void deleteLecturer(String username) {
        Lecturer lecturer = getLecturerByUsername(username);
        lecturer.setCampus(null);
        //lecturer.getCourses().clear();

        lecturerRepository.delete(lecturer);
    }

    @Override
    public Lecturer saveLecturer(Lecturer lecturer){
        return lecturerRepository.save(lecturer);
    }

    @Override
    public Campus getLecturerCampus(Long Id){
        Lecturer lecturer = getLecturerById(Id);
        Campus campus = lecturer.getCampus();
        return campus;
    }
}
