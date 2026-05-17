package com.foodmarket.notification.controller;

import com.foodmarket.notification.model.Notification;
import com.foodmarket.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationRepository notifRepo;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId) {
        log.info("[NOTIFICATION] Consultando notificaciones para usuario {}", userId);
        List<Notification> result = notifRepo.findByRecipientIdOrderByCreatedAtDesc(userId);
        log.info("[NOTIFICATION] {} notificaciones encontradas para usuario {}", result.size(), userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable Long userId) {
        log.info("[NOTIFICATION] Consultando no leidas para usuario {}", userId);
        List<Notification> result = notifRepo.findByRecipientIdAndReadFalse(userId);
        log.info("[NOTIFICATION] {} notificaciones no leidas para usuario {}", result.size(), userId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("[NOTIFICATION] Marcando como leida la notificacion {}", id);
        notifRepo.findById(id).ifPresent(n -> {
            n.setRead(true);
            notifRepo.save(n);
            log.info("[NOTIFICATION] Notificacion {} marcada como leida", id);
        });
        return ResponseEntity.ok().build();
    }
}
