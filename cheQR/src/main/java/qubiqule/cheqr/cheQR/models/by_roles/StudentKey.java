package qubiqule.cheqr.cheQR.models.by_roles;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class StudentKey implements Serializable {

    private String PK;

    public StudentKey(String PK) {
        this.PK = PK;
    }

    public StudentKey() {
    }

    public String getId() {
        return PK;
    }

    public void setId(String PK) {
        this.PK = PK;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((PK == null) ? 0 : PK.hashCode());
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
        StudentKey other = (StudentKey) obj;
        if (PK == null) {
            if (other.PK != null)
                return false;
        } else if (!PK.equals(other.PK))
            return false;
        return true;
    }

    
}
