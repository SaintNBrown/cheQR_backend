package qubiqule.cheqr.cheQR.service.servicesImpl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qubiqule.cheqr.cheQR.exceptions.EntityNotFoundException;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.repository.CourseRepository;
import qubiqule.cheqr.cheQR.repository.LecturerRepository;
import qubiqule.cheqr.cheQR.repository.StudentRepository;
import qubiqule.cheqr.cheQR.service.UserService;
import qubiqule.cheqr.cheQR.service.interfaces.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private LecturerRepository lecturerRepository;

    public Course createCourse(String courseCode, String courseTitle) throws UserPrincipalNotFoundException {
        Lecturer lecturer = (Lecturer) userService.getCurrentUser();

        Optional<Course> courseOpt = courseRepository.findByCourseCode(courseCode);

        if (courseOpt.isEmpty()) {
            Course course = new Course(courseCode, courseTitle);
            Course newCourse = courseRepository.save(course);

            newCourse.setLecturer(lecturer);
            
            Course savedCourse = courseRepository.save(newCourse);
            lecturerRepository.save(lecturer);

            return savedCourse;

        } else {
            return null;
        }
    }

    @Override
    public Student addStudentToCourse(String courseCode, String regNumber) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        
        Student student = studentRepository.findByRegNumber(regNumber)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        if(course.getStudents() == null){
            List<Student> students = new ArrayList<>();
            students.add(student);

            course.setStudents(students);
            courseRepository.save(course);
        } else {
            course.getStudents().add(student);
            courseRepository.save(course);
        }

        if(student.getCourses() == null){
            List<Course> courses = new ArrayList<>();
            courses.add(course);
            student.setCourses(courses);
            studentRepository.save(student);
        } else {
            student.getCourses().add(course);
            studentRepository.save(student);
        }
        return student;
    }

    @Override
    public List<Student> getStudentsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId.toString())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        return course.getStudents();
    }

    @Override
    public List<Course> getCoursesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return student.getCourses();
    }

    @Override
    public  Course findByCourseCode(String courseCode){
        Course course = courseRepository.findByCourseCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course not found!"));
        return course;
    }

    @Override
    public Course saveCourse(Course course){
        return courseRepository.save(course);
    }
}
