package qubiqule.cheqr.cheQR.models.business;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity
@Table(name = "attendance_list")
public class AttendanceList extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // One-to-one relationship with ClassSession
    @OneToOne
    @JoinColumn(name = "class_session_id", nullable = false)
    private ClassSession classSession;

    @Column(nullable = true)
    @OneToMany(mappedBy = "attendanceList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceRecord> attendanceRecords;

    public AttendanceList() {
    }

    public AttendanceList(Long id, ClassSession classSession, List<AttendanceRecord> attendanceRecords) {
        this.id = id;
        this.classSession = classSession;
        this.attendanceRecords = attendanceRecords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassSession getClassSession() {
        return classSession;
    }

    public void setClassSession(ClassSession classSession) {
        this.classSession = classSession;
    }

    public List<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }

    public void setAttendanceRecords(List<AttendanceRecord> attendanceRecords) {
        this.attendanceRecords = attendanceRecords;
    }

    @Override
    public String toString() {
        return "AttendanceList [id=" + id + ", classSession=" + classSession + "]";
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AttendanceList other = (AttendanceList) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (classSession == null) {
            if (other.classSession != null)
                return false;
        } else if (!classSession.equals(other.classSession))
            return false;
        if (attendanceRecords == null) {
            if (other.attendanceRecords != null)
                return false;
        } else if (!attendanceRecords.equals(other.attendanceRecords))
            return false;
        return true;
    }

    
}
