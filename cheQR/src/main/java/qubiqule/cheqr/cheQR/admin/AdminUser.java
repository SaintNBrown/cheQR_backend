package qubiqule.cheqr.cheQR.admin;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.business.Institution;

@Entity
@Table(name = "admin_users")
public class AdminUser extends User{

    @Getter
    @Setter
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Institution institution;
}
