package qubiqule.cheqr.cheQR.models.business;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity  
@Table(name = "campuses")  
public class Campus extends AbstractAuditingEntity<Long>{  

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  


    @Column(name = "campus_name", nullable = false)  
    private String campusName;   

    @ManyToOne  
    @JoinColumn(name = "institution_id", nullable = false)  
    private Institution institution;  

    @OneToMany(mappedBy = "campus", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false)  
    private List<Lecturer> lecturers = new LinkedList<>();  

    @OneToMany(mappedBy = "campus", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<Venue> venues = new LinkedList<>();

    public Campus() {
    }

    public Campus(Long id, String campusName, Institution institution, List<Lecturer> lecturers,
            List<Venue> venues) {
        this.id = id;
        this.campusName = campusName;
        this.institution = institution;
        this.lecturers = lecturers;
        this.venues = venues;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(List<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  
        if (!(obj instanceof Campus)) return false;  
        Campus that = (Campus) obj;  
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return "Campus [id=" + id + ", campusName=" + campusName + ", institution=" + institution + "]";
    }
}
