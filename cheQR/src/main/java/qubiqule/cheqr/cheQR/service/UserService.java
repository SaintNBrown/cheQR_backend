package qubiqule.cheqr.cheQR.service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.repository.UserRepository;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() throws UserPrincipalNotFoundException{
        //extract authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated()){
            String username = auth.getName();
            return userRepository.findByUsername(username).get();
        } 
        return null;
    }

    public ResponseEntity<Boolean> userExists(String username){

        Boolean userExists = userRepository.existsByUsername(username);

        if(!userExists){
            log.info("User does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } else {
            log.info("User exists!");
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } 

    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findById(Long Id){
        return userRepository.findById(Id);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
