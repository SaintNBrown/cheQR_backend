package qubiqule.cheqr.cheQR.service.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.repository.AttendanceListRepository;

@Service
public class AttendanceListService {
    
     @Autowired
    private AttendanceListRepository attendanceListRepository;

    // Get all attendance lists
    public List<AttendanceList> getAllAttendanceLists() {
        return attendanceListRepository.findAll();
    }

    // Get a specific attendance list by its ID
    public AttendanceList getAttendanceListById(Long id) {
        return attendanceListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance List not found with id: " + id));
    }

    // Delete an attendance list by its ID
    public void deleteAttendanceList(Long id) {
        attendanceListRepository.deleteById(id);
    }
}
