package com.app.bideo.controller.notification;

import com.app.bideo.auth.member.CustomUserDetails;
import com.app.bideo.dto.notification.NotificationResponseDTO;
import com.app.bideo.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationAPIController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<NotificationResponseDTO> notifications =
                notificationService.getNotifications(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }
}
