package org.samtuap.inong.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.samtuap.inong.domain.notification.dto.NotificationGetResponse;
import org.samtuap.inong.domain.notification.entity.SellerNotification;
import org.samtuap.inong.domain.notification.repository.SellerNotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final SellerNotificationRepository sellerNotificationRepository;

    public Page<NotificationGetResponse> getNotifications(Pageable pageable, boolean unread, Long memberId) {
        if(unread) {
            return sellerNotificationRepository.findAllBySellerIdAndIsRead(pageable, memberId, false).map(NotificationGetResponse::fromEntity);
        } else {
            return sellerNotificationRepository.findAllBySellerId(pageable, memberId).map(NotificationGetResponse::fromEntity);
        }
    }

    @Transactional
    public void readNotifications(Long memberId) {
        List<SellerNotification> notifications = sellerNotificationRepository.findAllBySellerIdAndIsRead(memberId, false);
        for (SellerNotification notification : notifications) {
            notification.updateIsRead(true);
        }
    }
}

