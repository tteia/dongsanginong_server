package org.samtuap.inong.domain.notification.dto;

import lombok.Builder;

@Builder
public record KafkaSellerNotificationRequest(Long sellerId, String title, String content, String url) {
}