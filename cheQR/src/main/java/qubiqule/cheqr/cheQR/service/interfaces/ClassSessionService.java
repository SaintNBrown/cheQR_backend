package qubiqule.cheqr.cheQR.service.interfaces;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

import qubiqule.cheqr.cheQR.models.business.ClassSession;

public interface ClassSessionService {

    ClassSession startClassSession(Long sessionId) throws Exception;

    ClassSession endClassSession(Long sessionId);

    ClassSession updateClassSession(ClassSession updatedClassSession) throws UserPrincipalNotFoundException;

    void deleteClassSession(String sessionId);

    List<ClassSession> getCurrentClassSessions()  throws UserPrincipalNotFoundException;

    ClassSession getClassSession(Long sessionId);
}
