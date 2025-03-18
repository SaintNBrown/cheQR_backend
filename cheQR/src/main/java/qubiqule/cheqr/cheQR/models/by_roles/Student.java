package qubiqule.cheqr.cheQR.models.by_roles;

import java.util.LinkedList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;
import qubiqule.cheqr.cheQR.models.business.Course;


@NoArgsConstructor  
@Entity  
@Table(name = "students")  
public class Student extends User {  

    @Column(name = "reg_number", unique = true)  
    @NotBlank
    private String regNumber;  

    @ManyToMany  
    @JoinTable(name = "student_courses",  
               joinColumns = @JoinColumn(name = "student_id"),  
               inverseJoinColumns = @JoinColumn(name = "course_code"))  
    private List<Course> courses = new LinkedList<>(); 

    
    @Getter
    @Setter
    @OneToMany(mappedBy = "student")
    private List<AttendanceRecord> attendanceRecords = new LinkedList<>();

    
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

}
