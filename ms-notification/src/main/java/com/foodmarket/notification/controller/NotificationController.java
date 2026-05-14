package com.foodmarket.notification.controller;
import com.foodmarket.notification.model.Notification; import com.foodmarket.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor; import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/notifications") @RequiredArgsConstructor @Slf4j
public class NotificationController {
    private final NotificationRepository notifRepo;
    @GetMapping("/{userId}") public ResponseEntity<List<Notification>> getByUser(@PathVariable Long userId) { return ResponseEntity.ok(notifRepo.findByRecipientIdOrderByCreatedAtDesc(userId)); }
    @GetMapping("/{userId}/unread") public ResponseEntity<List<Notification>> getUnread(@PathVariable Long userId) { return ResponseEntity.ok(notifRepo.findByRecipientIdAndReadFalse(userId)); }
}