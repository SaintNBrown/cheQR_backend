package qubiqule.cheqr.cheQR.models.business;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity
public class Schedule extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title")
    private String title;

    // The lecturer for this schedule
    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer lecturer;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ClassSession> classSessions;

    public Schedule() {
    }

    public Schedule(Long id, Lecturer lecturer, List<ClassSession> classSessions) {
        this.id = id;
        this.lecturer = lecturer;
        this.classSessions = classSessions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public List<ClassSession> getClassSessions() {
        return classSessions;
    }

    public void setClassSessions(List<ClassSession> classSessions) {
        this.classSessions = classSessions;
    }

    @Override
    public String toString() {
        return "Schedule [id=" + id + ", title=" + title + ", lecturer=" + lecturer + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lecturer == null) ? 0 : lecturer.hashCode());
        result = prime * result + ((classSessions == null) ? 0 : classSessions.hashCode());
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
        Schedule other = (Schedule) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (lecturer == null) {
            if (other.lecturer != null)
                return false;
        } else if (!lecturer.equals(other.lecturer))
            return false;
        if (classSessions == null) {
            if (other.classSessions != null)
                return false;
        } else if (!classSessions.equals(other.classSessions))
            return false;
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    

}