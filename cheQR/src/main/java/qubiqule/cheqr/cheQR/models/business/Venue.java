package qubiqule.cheqr.cheQR.models.business;


import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity  
@Table(name = "venues")  
public class Venue extends AbstractAuditingEntity<Long>{  

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  

    @Column(name = "hall_name", nullable = false)  
    private String hallName;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "first_corner_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "first_corner_longitude")),
        @AttributeOverride(name = "altitude", column = @Column(name = "first_corner_altitude"))
    })
    private GpsCordinates firstCornerCordinates;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "second_corner_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "second_corner_longitude")),
        @AttributeOverride(name = "altitude", column = @Column(name = "second_corner_altitude"))
    })
    private GpsCordinates secondCornerCordinates;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "third_corner_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "third_corner_longitude")),
        @AttributeOverride(name = "altitude", column = @Column(name = "third_corner_altitude"))
    })
    private GpsCordinates thirdCornerCordinates;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude", column = @Column(name = "fourth_corner_latitude")),
        @AttributeOverride(name = "longitude", column = @Column(name = "fourth_corner_longitude")),
        @AttributeOverride(name = "altitude", column = @Column(name = "fourth_corner_altitude"))
    })
    private GpsCordinates fourthCornerCordinates;

    @ManyToOne  
    @JoinColumn(name = "campus_id", nullable = false)  
    private Campus campus;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<ClassSession> classSessions;

    public Venue() {
    }

    public Venue(String hallName, GpsCordinates firstCornerCordinates,
            GpsCordinates secondCornerCordinates, GpsCordinates thirdCornerCordinates,
            GpsCordinates fourthCornerCordinates) {
        this.hallName = hallName;
        this.firstCornerCordinates = firstCornerCordinates;
        this.secondCornerCordinates = secondCornerCordinates;
        this.thirdCornerCordinates = thirdCornerCordinates;
        this.fourthCornerCordinates = fourthCornerCordinates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public GpsCordinates getFirstCornerCordinates() {
        return firstCornerCordinates;
    }

    public void setFirstCornerCordinates(GpsCordinates firstCornerCordinates) {
        this.firstCornerCordinates = firstCornerCordinates;
    }

    public GpsCordinates getSecondCornerCordinates() {
        return secondCornerCordinates;
    }

    public void setSecondCornerCordinates(GpsCordinates secondCornerCordinates) {
        this.secondCornerCordinates = secondCornerCordinates;
    }

    public GpsCordinates getThirdCornerCordinates() {
        return thirdCornerCordinates;
    }

    public void setThirdCornerCordinates(GpsCordinates thirdCornerCordinates) {
        this.thirdCornerCordinates = thirdCornerCordinates;
    }

    public GpsCordinates getFourthCornerCordinates() {
        return fourthCornerCordinates;
    }

    public void setFourthCornerCordinates(GpsCordinates fourthCornerCordinates) {
        this.fourthCornerCordinates = fourthCornerCordinates;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public List<ClassSession> getClassSessions() {
        return classSessions;
    }

    public void setClassSessions(List<ClassSession> classSessions) {
        this.classSessions = classSessions;
    }

    @Override
    public String toString() {
        return "Venue [id=" + id + ", hallName=" + hallName
                + ", firstCornerCordinates=" + firstCornerCordinates + ", secondCornerCordinates=" + secondCornerCordinates
                + ", thirdCornerCordinates=" + thirdCornerCordinates + ", fourthCornerCordinates=" + fourthCornerCordinates
                + ", campus=" + campus + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((firstCornerCordinates == null) ? 0 : firstCornerCordinates.hashCode());
        result = prime * result + ((secondCornerCordinates == null) ? 0 : secondCornerCordinates.hashCode());
        result = prime * result + ((thirdCornerCordinates == null) ? 0 : thirdCornerCordinates.hashCode());
        result = prime * result + ((fourthCornerCordinates == null) ? 0 : fourthCornerCordinates.hashCode());
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
        Venue other = (Venue) obj;
        if (hallName == null) {
            if (other.hallName != null)
                return false;
        } else if (!hallName.equals(other.hallName))
            return false;
        if (firstCornerCordinates == null) {
            if (other.firstCornerCordinates != null)
                return false;
        } else if (!firstCornerCordinates.equals(other.firstCornerCordinates))
            return false;
        if (secondCornerCordinates == null) {
            if (other.secondCornerCordinates != null)
                return false;
        } else if (!secondCornerCordinates.equals(other.secondCornerCordinates))
            return false;
        if (thirdCornerCordinates == null) {
            if (other.thirdCornerCordinates != null)
                return false;
        } else if (!thirdCornerCordinates.equals(other.thirdCornerCordinates))
            return false;
        if (fourthCornerCordinates == null) {
            if (other.fourthCornerCordinates != null)
                return false;
        } else if (!fourthCornerCordinates.equals(other.fourthCornerCordinates))
            return false;
        return true;
    }

    
}
