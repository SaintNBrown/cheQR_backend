package qubiqule.cheqr.cheQR.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.business.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
