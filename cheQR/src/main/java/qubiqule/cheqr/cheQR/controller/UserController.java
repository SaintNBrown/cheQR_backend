package qubiqule.cheqr.cheQR.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;
import qubiqule.cheqr.cheQR.mapping.MappingUtil;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.service.UserService;
import qubiqule.cheqr.cheQR.service.interfaces.LecturerService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private MappingUtil mappingUtil;

    @GetMapping("/users/me")
    public ResponseEntity<User> getCurrentUser() throws UserPrincipalNotFoundException{
        User user = userService.getCurrentUser();

        if(user != null){
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/lecturer/getLecturer")
    public LecturerDTO getLecturerById(@RequestParam String id){
        return mappingUtil.mapToLecturerDTO(lecturerService.getLecturerById(Long.parseLong(id)));
    }
}
