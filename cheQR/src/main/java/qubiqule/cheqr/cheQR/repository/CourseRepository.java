package qubiqule.cheqr.cheQR.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.business.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>{
        // Find a course by its course code
        Optional<Course> findByCourseCode(String courseCode);
    
        // Check if a course exists by its course code
        boolean existsByCourseCode(String courseCode);
}
