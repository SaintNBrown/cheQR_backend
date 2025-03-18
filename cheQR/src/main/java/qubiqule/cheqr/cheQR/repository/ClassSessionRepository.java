package qubiqule.cheqr.cheQR.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.business.ClassSession;

@Repository
public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    List<ClassSession> findByStartTimeBetweenAndIsActiveFalseAndNotifiedBeforeStartFalse(LocalTime start,
            LocalTime end);

    // @Query("SELECT c FROM ClassSession c JOIN FETCH c.attendanceList a WHERE
    // c.startTime < :now AND c.isActive = false")
    List<ClassSession> findByStartTimeEqualsAndIsActiveFalse(LocalTime time);

    List<ClassSession> findByEndTimeBeforeAndIsActiveTrueAndHasEndedFalse(LocalTime time);

    @Query("SELECT cs FROM class_session cs " +
            "WHERE cs.course IN (SELECT c FROM Course c WHERE c.lecturer.id = :lecturerId) " +
            "AND cs.date = :currentDate " +
            "AND cs.startTime <= :currentTime " +
            "AND cs.endTime > :currentTime " +
            "AND cs.isActive = true " +
            "AND cs.hasEnded = false")
    List<ClassSession> findOngoingSessionsForLecturer(@Param("lecturerId") Long lecturerId,
            @Param("currentDate") LocalDate currentDate,
            @Param("currentTime") LocalTime currentTime);
}