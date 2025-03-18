package qubiqule.cheqr.cheQR.models.business;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity(name = "class_session")
@Table(name = "class_session", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "course_code", "sessionDate" })
})
@NoArgsConstructor
public class ClassSession extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek dayOfWeek;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    // A session is related to one course
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_code")
    private Course course;
    
    // A session has one attendance list
    @OneToOne(mappedBy = "classSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private AttendanceList attendanceList;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = true)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ClassSessionStatus status = ClassSessionStatus.SCHEDULED;

    @Getter
    @Setter
    private boolean isActive;

    @Getter
    @Setter
    private boolean notifiedBeforeStart;

    @Getter
    @Setter
    private boolean hasEnded = false;

    @Column
    private String qrCodeString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public AttendanceList getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(AttendanceList attendanceList) {
        this.attendanceList = attendanceList;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public ClassSessionStatus getStatus() {
        return status;
    }

    public void setStatus(ClassSessionStatus status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getQrCodeString() {
        return qrCodeString;
    }

    public void setQrCodeString(String qrCodeString) {
        this.qrCodeString = qrCodeString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + ((course == null) ? 0 : course.hashCode());
        result = prime * result + ((attendanceList == null) ? 0 : attendanceList.hashCode());
        result = prime * result + ((venue == null) ? 0 : venue.hashCode());
        result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + (isActive ? 1231 : 1237);
        result = prime * result + (notifiedBeforeStart ? 1231 : 1237);
        result = prime * result + (hasEnded ? 1231 : 1237);
        result = prime * result + ((qrCodeString == null) ? 0 : qrCodeString.hashCode());
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
        ClassSession other = (ClassSession) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (dayOfWeek != other.dayOfWeek)
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (course == null) {
            if (other.course != null)
                return false;
        } else if (!course.equals(other.course))
            return false;
        if (attendanceList == null) {
            if (other.attendanceList != null)
                return false;
        } else if (!attendanceList.equals(other.attendanceList))
            return false;
        if (venue == null) {
            if (other.venue != null)
                return false;
        } else if (!venue.equals(other.venue))
            return false;
        if (schedule == null) {
            if (other.schedule != null)
                return false;
        } else if (!schedule.equals(other.schedule))
            return false;
        if (status != other.status)
            return false;
        if (isActive != other.isActive)
            return false;
        if (notifiedBeforeStart != other.notifiedBeforeStart)
            return false;
        if (hasEnded != other.hasEnded)
            return false;
        if (qrCodeString == null) {
            if (other.qrCodeString != null)
                return false;
        } else if (!qrCodeString.equals(other.qrCodeString))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ClassSession [id=" + id + ", dayOfWeek=" + dayOfWeek + ", date=" + date + ", startTime=" + startTime
                + ", endTime=" + endTime + ", course=" + course + ", venue="
                + venue + ", schedule=" + schedule + ", status=" + status + ", isActive=" + isActive
                + ", hasEnded=" + hasEnded + ", qrCodeString="
                + qrCodeString + "]";
    }

}
