package qubiqule.cheqr.cheQR.models.business;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import qubiqule.cheqr.cheQR.admin.AdminUser;
import qubiqule.cheqr.cheQR.sharedkernel.domain.AbstractAuditingEntity;

@Entity  
@Table(name = "institutions")  
public class Institution extends AbstractAuditingEntity<Long>{  

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;  

    @Column(name = "institution_name", nullable = false)  
    private String institutionName;    

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true)  
    private List<Campus> campuses = new LinkedList<>();

    @OneToOne(mappedBy = "institution")
    @JsonBackReference
    private AdminUser adminUser;

    public Institution() {
    }

    public Institution(Long id, String institutionName, List<Campus> campuses) {
        this.id = id;
        this.institutionName = institutionName;
        this.campuses = campuses;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  
        if (!(obj instanceof Institution)) return false;  
        Institution that = (Institution) obj;  
        return Objects.equals(getId(), that.getId()); 
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    @Override
    public String toString() {
        return "Institution [id=" + id + ", institutionName=" + institutionName + "]";
    }

    public List<Campus> getCampuses() {
        return campuses;
    }

    public void setCampuses(List<Campus> campuses) {
        this.campuses = campuses;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    } 
    
}
