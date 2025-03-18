package qubiqule.cheqr.cheQR.admin.payload.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampusDTO {

    String campusId;

    String campusName;

    String institutionId;

    @ToString.Exclude
    List<VenueDTO> venueDTOs;

    @ToString.Exclude
    List<LecturerDTO> lecturerDTOs;

    public CampusDTO(String campusName){
        this.campusName = campusName;
    }
}
