package org.samtuap.inong.domain.notification.dto;

import lombok.Builder;
import org.samtuap.inong.domain.notification.entity.SellerNotification;

@Builder
public record NotificationGetResponse(Long notificationId,
                                      String title,
                                      String content,
                                      boolean isRead,
                                      String url) {

    public static NotificationGetResponse fromEntity(SellerNotification notification) {
        return NotificationGetResponse.builder()
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .notificationId(notification.getId())
                .url(notification.getUrl())
                .build();
    }
}