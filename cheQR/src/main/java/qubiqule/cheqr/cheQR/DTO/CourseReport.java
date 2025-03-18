package qubiqule.cheqr.cheQR.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseReport {
    private Long courseId;
    private String courseCode;
    private String courseTitle;
    private List<SessionDTO> sessions;
}
