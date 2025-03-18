package qubiqule.cheqr.cheQR.mapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qubiqule.cheqr.cheQR.DTO.AdminUserDTO;
import qubiqule.cheqr.cheQR.DTO.AttendanceListDTO;
import qubiqule.cheqr.cheQR.DTO.AttendanceRecordsDTO;
import qubiqule.cheqr.cheQR.DTO.CampusInstitutionDTO;
import qubiqule.cheqr.cheQR.DTO.CourseDTO;
import qubiqule.cheqr.cheQR.DTO.GpsDTO;
import qubiqule.cheqr.cheQR.DTO.SessionDTO;
import qubiqule.cheqr.cheQR.DTO.StudentDTO;
import qubiqule.cheqr.cheQR.DTO.UserDTO;
import qubiqule.cheqr.cheQR.DTO.schedule.ScheduleDTO;
import qubiqule.cheqr.cheQR.admin.AdminUser;
import qubiqule.cheqr.cheQR.admin.payload.request.CampusDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.InstitutionDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.LecturerDTO;
import qubiqule.cheqr.cheQR.admin.payload.request.VenueDTO;
import qubiqule.cheqr.cheQR.models.User;
import qubiqule.cheqr.cheQR.models.business.AttendanceList;
import qubiqule.cheqr.cheQR.models.business.AttendanceRecord;
import qubiqule.cheqr.cheQR.models.business.Campus;
import qubiqule.cheqr.cheQR.models.business.ClassSession;
import qubiqule.cheqr.cheQR.models.business.Course;
import qubiqule.cheqr.cheQR.models.business.GpsCordinates;
import qubiqule.cheqr.cheQR.models.business.Institution;
import qubiqule.cheqr.cheQR.models.business.Schedule;
import qubiqule.cheqr.cheQR.models.business.Venue;
import qubiqule.cheqr.cheQR.models.by_roles.Lecturer;
import qubiqule.cheqr.cheQR.models.by_roles.Student;
import qubiqule.cheqr.cheQR.repository.CampusRepository;
import qubiqule.cheqr.cheQR.repository.InstitutionRepository;

@Component
public class MappingUtil {

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private CampusRepository campusRepository;

    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId().toString());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(List.copyOf(user.getRoles()));
        userDTO.setIsVerified(Boolean.toString(user.isVerified()));

        return userDTO;
    }

    public List<UserDTO> mapUserDTOs(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            userDTOs.add(mapToUserDTO(user));
        }

        return userDTOs;
    }

    public AdminUserDTO mapToAdminUserDTO(AdminUser adminUser) {
        if (adminUser == null) {
            return null;
        }

        AdminUserDTO adminUserDTO = new AdminUserDTO();
        adminUserDTO.setFirstName(adminUser.getFirstName());
        adminUserDTO.setLastName(adminUser.getLastName());
        adminUserDTO.setUsername(adminUser.getUsername());
        adminUserDTO.setEmail(adminUser.getEmail());
        adminUserDTO.setPassword(adminUser.getPassword());
        adminUserDTO.setRoles(List.copyOf(adminUser.getRoles()));
        adminUserDTO.setIsVerified(Boolean.toString(adminUser.isVerified()));
        adminUserDTO.setId(adminUser.getId().toString());
        adminUserDTO.setInstitutionDTO(mapToInstitutionDTO(adminUser.getInstitution()));

        return adminUserDTO;
    }

    public List<AdminUserDTO> mapAdminUserDTOs(List<AdminUser> adminUsers) {
        if (adminUsers == null) {
            return null;
        }
        List<AdminUserDTO> adminUserDTOs = new ArrayList<>();

        for (AdminUser adminUser : adminUsers) {
            adminUserDTOs.add(mapToAdminUserDTO(adminUser));
        }

        return adminUserDTOs;
    }

    public Venue mapToVenue(VenueDTO venueDTO) {
        if (venueDTO == null) {
            return null;
        }

        Venue venue = new Venue();
        venue.setCampus(campusRepository.findById(venueDTO.getCampusId()).isPresent()
                ? campusRepository.findById(venueDTO.getCampusId()).get()
                : null);
        venue.setHallName(venueDTO.getHallname());
        venue.setFirstCornerCordinates(mapToGpsCordinates(venueDTO.getFirstCornerCordinates()));
        venue.setSecondCornerCordinates(mapToGpsCordinates(venueDTO.getSecondCornerCordinates()));
        venue.setThirdCornerCordinates(mapToGpsCordinates(venueDTO.getThirdCornerCordinates()));
        venue.setFourthCornerCordinates(mapToGpsCordinates(venueDTO.getFourthCornerCordinates()));
        return venue;
    }

    public VenueDTO mapToVenueDTO(Venue venue) {
        // Return a VenueDTO for the given Venue
        VenueDTO venueDTO = new VenueDTO();
        venueDTO.setVenueId(venue.getId().toString());
        venueDTO.setCampusId(venue.getCampus().getId());
        venueDTO.setHallname(venue.getHallName());
        venueDTO.setFirstCornerCordinates(mapToGpsDTO(venue.getFirstCornerCordinates()));
        venueDTO.setSecondCornerCordinates(mapToGpsDTO(venue.getSecondCornerCordinates()));
        venueDTO.setThirdCornerCordinates(mapToGpsDTO(venue.getThirdCornerCordinates()));
        venueDTO.setFourthCornerCordinates(mapToGpsDTO(venue.getFourthCornerCordinates()));
        return venueDTO;
    }

    public List<VenueDTO> mapToVenueDTOs(List<Venue> venues) {
        if (venues == null) {
            return null;
        }

        List<VenueDTO> venueDTOs = new ArrayList<>();

        for (Venue venue : venues) {
            venueDTOs.add(mapToVenueDTO(venue));
        }

        return venueDTOs;
    }

    public GpsDTO mapToGpsDTO(GpsCordinates gpsCordinates){
        if(gpsCordinates == null){
            return null;
        }

        GpsDTO gpsDTO = new GpsDTO();

        gpsDTO.setAltitude(gpsCordinates.getAltitude());
        gpsDTO.setLatitude(gpsCordinates.getLatitude());
        gpsDTO.setLongitude(gpsCordinates.getLongitude());

        return gpsDTO;
    }

    public GpsCordinates mapToGpsCordinates(GpsDTO gpsDTO){
        if(gpsDTO == null){
            return null;
        }

        GpsCordinates gpsCordinates = new GpsCordinates(
            gpsDTO.getLatitude(), 
            gpsDTO.getLongitude(), 
            gpsDTO.getAltitude());

            return gpsCordinates;
    }

    // ===================================================

    public Lecturer mapToLecturer(LecturerDTO lecturerDTO) {
        if (lecturerDTO == null) {
            return null;
        }

        Lecturer lecturer = new Lecturer();

        lecturer.setEmail(lecturerDTO.getEmail());
        lecturer.setFirstName(lecturerDTO.getFirstName());
        lecturer.setLastName(lecturerDTO.getLastName());
        lecturer.setUsername(lecturerDTO.getUsername());

        return lecturer;
    }

    public LecturerDTO mapToLecturerDTO(Lecturer lecturer) {
        if (lecturer == null) {
            return null;
        }

        LecturerDTO lecturerDTO = new LecturerDTO();

        lecturerDTO.setId(lecturer.getId().toString());
        lecturerDTO.setUsername(lecturer.getUsername());
        lecturerDTO.setFirstName(lecturer.getFirstName());
        lecturerDTO.setLastName(lecturer.getLastName());
        lecturerDTO.setEmail(lecturer.getEmail());

        return lecturerDTO;
    }

    public List<LecturerDTO> mapToLecturerDTOs(List<Lecturer> lecturers) {
        if (lecturers == null) {
            return null;
        }

        List<LecturerDTO> lecturerDTOs = new ArrayList<>();
        for (Lecturer lecturer : lecturers) {
            lecturerDTOs.add(mapToLecturerDTO(lecturer));
        }

        return lecturerDTOs;
    }

    // =======================================================

    public InstitutionDTO mapToInstitutionDTO(Institution institution) {

        if (institution == null) {
            return null;
        }

        List<CampusDTO> campusDTOs = !(institution.getCampuses() == null) ? institution.getCampuses().stream()
                .map(this::mapToCampusDTO)
                .collect(Collectors.toList()) : null;

        // Return the InstitutionDTO
        return new InstitutionDTO(
                institution.getId(),
                institution.getInstitutionName(),
                campusDTOs);
    }

    public Institution mapToInstitution(InstitutionDTO institutionDto) {
        List<Campus> campuses = !(institutionDto.getCampuses() == null) ? institutionDto.getCampuses().stream()
                .map(this::mapToCampus)
                .collect(Collectors.toList()) : null;

        // Return the InstitutionDTO
        return new Institution(
                institutionDto.getId(),
                institutionDto.getName(),
                campuses);
    }

    public List<InstitutionDTO> mapTInstitutionDTOs(List<Institution> institutions) {
        if (institutions == null) {
            return null;
        }

        List<InstitutionDTO> institutionDTOs = new ArrayList<>();

        for (Institution institution : institutions) {
            institutionDTOs.add(mapToInstitutionDTO(institution));
        }

        return institutionDTOs;

    }

    // =================================================================

    public CampusDTO mapToCampusDTO(Campus campus) {
        // Return a CampusDTO for the given Campus
        CampusDTO campusDTO = new CampusDTO(
                campus.getId().toString(),
                campus.getCampusName().toString(),
                campus.getInstitution() != null ? campus.getInstitution().getId().toString() : null, // Institution ID
                mapToVenueDTOs(campus.getVenues()),
                mapToLecturerDTOs(campus.getLecturers()));

        return campusDTO;
    }

    public Campus mapToCampus(CampusDTO campusDTO) {
        List<Venue> venues = !(campusDTO.getVenueDTOs() == null) ? campusDTO.getVenueDTOs().stream()
                .map(this::mapToVenue)
                .collect(Collectors.toList()) : null;

        List<Lecturer> lecturers = !(campusDTO.getLecturerDTOs() == null) ? campusDTO.getLecturerDTOs().stream()
                .map(this::mapToLecturer)
                .collect(Collectors.toList()) : null;

        Campus campus = new Campus();
        campus.setId(Long.parseLong(campusDTO.getCampusId()));
        campus.setCampusName(campusDTO.getCampusName());
        campus.setVenues(venues);
        campus.setLecturers(lecturers);

        Institution institution = institutionRepository.findById(Long.parseLong(campusDTO.getInstitutionId()))
                .orElseThrow();
        campus.setInstitution(institution);

        return campus;
    }

    public List<CampusDTO> mapToCampusDTOs(List<Campus> campuses) {
        if (campuses == null) {
            return null;
        }

        List<CampusDTO> campusDTOs = new ArrayList<>();

        for (Campus campus : campuses) {
            campusDTOs.add(mapToCampusDTO(campus));
        }

        return campusDTOs;
    }

    // ===============================================================

    public CourseDTO mapToCourseDto(Course course) {
        if (course == null) {
            return null;
        }

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId().toString());
        courseDTO.setCourseCode(course.getCourseCode());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setLecturerDTO(mapToLecturerDTO(course.getLecturer()));

        return courseDTO;
    }

    public Course mapToCourse(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course course = new Course();
        course.setCourseCode(courseDTO.getCourseCode());
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setLecturer(mapToLecturer(courseDTO.getLecturerDTO()));

        return course;
    }

    public List<CourseDTO> mapToCourseDTOs(List<Course> courses){
        if(courses == null){
            return null;
        }

        List<CourseDTO> courseDTOs = new ArrayList<>();

        for (Course course : courses) {
            courseDTOs.add(mapToCourseDto(course));
        }

        return courseDTOs;
    }

    public List<Course> mapToCourses(List<CourseDTO> courseDTOs){
        if(courseDTOs == null){
            return null;
        }

        List<Course> courses = new ArrayList<>();

        for (CourseDTO courseDTO : courseDTOs) {
            courses.add(mapToCourse(courseDTO));
        }

        return courses;
    }

    // =============================================-
    public StudentDTO mapToStudentDTO(Student student) {
        if (student == null) {
            return null;
        }

        StudentDTO studentDTO = new StudentDTO();

        studentDTO.setId(student.getId().toString());
        studentDTO.setFirstName(student.getFirstName());
        studentDTO.setLastName(student.getLastName());
        studentDTO.setRegNumber(student.getRegNumber());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setCourses(mapToCourseDTOs(student.getCourses()));

        return studentDTO;
    }

    public Student mapToStudent(StudentDTO studentDto) {
        if (studentDto == null) {
            return null;
        }

        Student student = new Student();

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setRegNumber(studentDto.getRegNumber());
        student.setEmail(studentDto.getEmail());
        student.setCourses(mapToCourses(studentDto.getCourses()));

        return student;
    }

    public List<StudentDTO> mapToStudentDTOs(List<Student> students) {
        if (students == null) {
            return null;
        }

        List<StudentDTO> studentDTOs = new ArrayList<>();

        for(Student student: students){
            studentDTOs.add(mapToStudentDTO(student));
        }

        return studentDTOs;

    }

    public List<Student> mapToStudents(List<StudentDTO> studentDTOs) {
        if (studentDTOs == null) {
            return null;
        }

        List<Student> students = studentDTOs.stream().map(this::mapToStudent).collect(Collectors.toList());

        return students;
    }

    // ======================================

    public AttendanceRecordsDTO mapToAttendanceRecordsDTO(AttendanceRecord attendanceRecord) {
        if (attendanceRecord == null) {
            return null;
        }

        AttendanceRecordsDTO recordsDTO = new AttendanceRecordsDTO();
        recordsDTO.setId(attendanceRecord.getId().toString());
        recordsDTO.setAttendanceStatus(attendanceRecord.getAttendanceStatus());
        recordsDTO.setStudentDTO(mapToStudentDTO(attendanceRecord.getStudent()));

        return recordsDTO;
    }

    public AttendanceRecord mapToAttendanceRecord(AttendanceRecordsDTO attendanceRecordDto) {
        if (attendanceRecordDto == null) {
            return null;
        }

        AttendanceRecord record = new AttendanceRecord();
        record.setAttendanceStatus(attendanceRecordDto.getAttendanceStatus());
        record.setStudent(mapToStudent(attendanceRecordDto.getStudentDTO()));

        return record;
    }

    public List<AttendanceRecord> toRecords(List<AttendanceRecordsDTO> attendanceRecordsDTOs) {
        if (attendanceRecordsDTOs == null) {
            return null;
        }
        List<AttendanceRecord> attendanceRecords = attendanceRecordsDTOs.stream().map(this::mapToAttendanceRecord)
                .collect(Collectors.toList());
        return attendanceRecords;

    }

    public List<AttendanceRecordsDTO> toRecordDTOs(List<AttendanceRecord> attendanceRecord) {
        if (attendanceRecord == null) {
            return null;
        }
        List<AttendanceRecordsDTO> attendanceRecordsDto = attendanceRecord.stream().map(this::mapToAttendanceRecordsDTO)
                .collect(Collectors.toList());
        return attendanceRecordsDto;

    }

    // =============================================

    public AttendanceListDTO mapToAttendanceListDTO(AttendanceList attendanceList) {
        if (attendanceList == null) {
            return null;
        }

        AttendanceListDTO attendanceListDTO = new AttendanceListDTO();
        attendanceListDTO.setId(attendanceList.getId().toString());
        attendanceListDTO.setAttendanceRecordDTOs(toRecordDTOs(attendanceList.getAttendanceRecords()));

        return attendanceListDTO;
    }

    public AttendanceList mapToAttendanceList(AttendanceListDTO attendanceListDto) {
        if (attendanceListDto == null) {
            return null;
        }

        AttendanceList attendanceList = new AttendanceList();
        attendanceList.setAttendanceRecords(toRecords(attendanceListDto.getAttendanceRecordDTOs()));

        return attendanceList;
    }

    // ===========================================

    public SessionDTO mapToSessionDTO(ClassSession classSession) {
        if (classSession == null) {
            return null;
        }

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(classSession.getId().toString());
        sessionDTO.setAttendanceListDTO(mapToAttendanceListDTO(classSession.getAttendanceList()));
        sessionDTO.setCourseDTO(mapToCourseDto(classSession.getCourse()));
        sessionDTO.setDayOfWeek(classSession.getDayOfWeek().toString());
        sessionDTO.setEndTime(classSession.getEndTime());
        sessionDTO.setIsActive(String.valueOf(classSession.isActive()));
        sessionDTO.setStartTime(classSession.getStartTime());
        sessionDTO.setStatus(classSession.getStatus());
        sessionDTO.setVenueDTO(mapToVenueDTO(classSession.getVenue()));
        sessionDTO.setDate(String.valueOf(classSession.getDate()));
        sessionDTO.setQrCodeString(classSession.getQrCodeString());

        return sessionDTO;
    }

    public ClassSession mapToSession(SessionDTO sessionDTO) {
        if (sessionDTO == null) {
            return null;
        }

        ClassSession session = new ClassSession();
        session.setAttendanceList(mapToAttendanceList(sessionDTO.getAttendanceListDTO()));
        session.setCourse(mapToCourse(sessionDTO.getCourseDTO()));
        session.setDayOfWeek(DayOfWeek.valueOf(sessionDTO.getDayOfWeek()));
        session.setEndTime(sessionDTO.getEndTime());

        session.setStartTime(sessionDTO.getStartTime());
        session.setStatus(sessionDTO.getStatus());
        session.setVenue(mapToVenue(sessionDTO.getVenueDTO()));
        session.setDate(LocalDate.parse(sessionDTO.getDate()));

        return session;
    }

    public List<ClassSession> mapToSessions(List<SessionDTO> sessionDTOs) {
        if (sessionDTOs == null) {
            return null;
        }
        List<ClassSession> sessions = sessionDTOs.stream().map(this::mapToSession).collect(Collectors.toList());
        return sessions;
    }

    public List<SessionDTO> mapToSessionDTOs(List<ClassSession> sessions) {
        if (sessions == null) {
            return null;
        }
        List<SessionDTO> sessionDTOs = sessions.stream().map(this::mapToSessionDTO).collect(Collectors.toList());
        return sessionDTOs;
    }

    // =======================================
    public ScheduleDTO mapToScheduleDTO(Schedule schedule) {
        if (schedule == null) {
            return null;
        }

        ScheduleDTO scheduleDTO = new ScheduleDTO();

        scheduleDTO.setId(schedule.getId().toString());
        scheduleDTO.setClassSessions(mapToSessionDTOs(schedule.getClassSessions()));
        scheduleDTO.setTitle(schedule.getTitle());

        return scheduleDTO;

    }

    public Schedule mapToSchedule(ScheduleDTO scheduleDto) {
        if (scheduleDto == null) {
            return null;
        }
        Schedule schedule = new Schedule();
        schedule.setClassSessions(mapToSessions(scheduleDto.getClassSessions()));
        schedule.setTitle(schedule.getTitle());
        return schedule;
    }

    public List<ScheduleDTO> mapToScheduleDTOs(List<Schedule> schedules) {
        if (schedules == null) {
            return null;
        }
        List<ScheduleDTO> scheduleDTOs = schedules.stream().map(this::mapToScheduleDTO).collect(Collectors.toList());
        return scheduleDTOs;
    }

    public CampusInstitutionDTO mapToCampusInstitutionDTO(Campus campus){
        if(campus == null){
            return null;
        }

        return new CampusInstitutionDTO(campus.getInstitution().getInstitutionName());
    }

}
