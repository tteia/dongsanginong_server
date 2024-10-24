package org.samtuap.inong.domain.notification.api;

import lombok.RequiredArgsConstructor;
import org.samtuap.inong.domain.notification.dto.NotificationGetResponse;
import org.samtuap.inong.domain.notification.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/notification")
@RequiredArgsConstructor
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Page<NotificationGetResponse>> getNotification(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                         @RequestParam(value = "unread", required = false) boolean unread,
                                                                         @RequestHeader("sellerId") Long sellerId) {
        Page<NotificationGetResponse> notifications = notificationService.getNotifications(pageable, unread, sellerId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/read")
    public void readNotifications(@RequestHeader("sellerId") Long sellerId) {
        notificationService.readNotifications(sellerId);
    }

}
