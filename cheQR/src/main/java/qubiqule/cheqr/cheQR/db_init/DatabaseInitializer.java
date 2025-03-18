package qubiqule.cheqr.cheQR.db_init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import qubiqule.cheqr.cheQR.models.ERole;
import qubiqule.cheqr.cheQR.models.Role;
import qubiqule.cheqr.cheQR.repository.RoleRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        //insert roles into roles table if they don't exist
        if(roleRepository.count() == 0){
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
            roleRepository.save(new Role(ERole.ROLE_STUDENT));
            roleRepository.save(new Role(ERole.ROLE_LECTURER));
        }
    }
}
