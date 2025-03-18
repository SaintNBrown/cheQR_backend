package qubiqule.cheqr.cheQR.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    String id;
    String courseCode;
    String courseTitle;
    List<StudentDTO> studentDTOs;
    LecturerDTO lecturerDTO;
}
