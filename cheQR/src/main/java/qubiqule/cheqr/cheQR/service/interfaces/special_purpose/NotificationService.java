package qubiqule.cheqr.cheQR.service.interfaces.special_purpose;

import qubiqule.cheqr.cheQR.models.business.ClassSession;

public interface NotificationService {
    void sendPreNotification(ClassSession session);
    void sendStartNotification(ClassSession session);
    public void sendEndNotification(ClassSession session);
}
