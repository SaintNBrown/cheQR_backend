package qubiqule.cheqr.cheQR.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import qubiqule.cheqr.cheQR.models.Role;

import java.util.Optional;
import qubiqule.cheqr.cheQR.models.ERole;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByName(ERole name);

    @Query("SELECT r FROM Role r WHERE r.name IN :roleNames")
    List<Role> findAllByName(@Param("roleNames") List<ERole> ERoles);
}
