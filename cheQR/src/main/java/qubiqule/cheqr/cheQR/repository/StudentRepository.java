package qubiqule.cheqr.cheQR.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.by_roles.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Find a student by their registration number
    Optional<Student> findByRegNumber(String regNumber);

    // Check if a student exists by their registration number
    boolean existsByRegNumber(String regNumber);
}
