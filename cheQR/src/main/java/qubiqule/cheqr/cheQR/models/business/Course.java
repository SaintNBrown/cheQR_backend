package qubiqule.cheqr.cheQR.models.business;


import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity
@Table(name = "courses")
public class Course extends AbstractAuditingEntity<String> {

    @Id
    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "course_title", nullable = false)
    private String courseTitle;

    @ManyToMany(mappedBy = "courses")
    @Column(nullable = true)
    private List<Student> students = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = true)
    private Lecturer lecturer;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = true)
    private List<ClassSession> classSessions = new LinkedList<>();

    public Course(String courseCode, String courseTitle, List<Student> students, Lecturer lecturer,
            List<ClassSession> classSessions) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.students = students;
        this.lecturer = lecturer;
        this.classSessions = classSessions;
    }

    public Course(String courseCode, String courseTitle) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
    }

    public Course() {
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
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
        return "Course [courseCode=" + courseCode + ", courseTitle=" + courseTitle + ", lecturer=" + lecturer + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((courseCode == null) ? 0 : courseCode.hashCode());
        result = prime * result + ((courseTitle == null) ? 0 : courseTitle.hashCode());
        result = prime * result + ((students == null) ? 0 : students.hashCode());
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
        Course other = (Course) obj;
        if (courseCode == null) {
            if (other.courseCode != null)
                return false;
        } else if (!courseCode.equals(other.courseCode))
            return false;
        if (courseTitle == null) {
            if (other.courseTitle != null)
                return false;
        } else if (!courseTitle.equals(other.courseTitle))
            return false;
        if (students == null) {
            if (other.students != null)
                return false;
        } else if (!students.equals(other.students))
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

    @Override
    public String getId() {
       return "";
    }

    
}
