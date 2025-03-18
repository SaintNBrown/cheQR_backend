package qubiqule.cheqr.cheQR.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventNotificationService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter registerEmitter(Long lecturerId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(lecturerId, emitter);
        emitter.onCompletion(() -> emitters.remove(lecturerId));
        emitter.onTimeout(() -> emitters.remove(lecturerId));
        log.info("Lecturer connected!.");
        return emitter;
    }

    public void sendNotification(Long lecturerId, Object data) {
        SseEmitter emitter = emitters.get(lecturerId);
        if (emitter != null) {
            try {
                emitter.send(data);
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(lecturerId);
            }
        }
    }
}
