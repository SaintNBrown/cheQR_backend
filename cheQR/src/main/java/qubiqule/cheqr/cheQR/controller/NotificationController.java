package qubiqule.cheqr.cheQR.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import qubiqule.cheqr.cheQR.service.EventNotificationService;

@RestController
@RequestMapping("/api")
public class NotificationController {
    @Autowired
    private EventNotificationService eventNotificationService;

    @GetMapping("/notifications/connect/{lecturerId}")
    public SseEmitter subscribeToNotifications(@PathVariable Long lecturerId) {
        // Register a new SSE emitter for the lecturer
        return eventNotificationService.registerEmitter(lecturerId);
    }
}
