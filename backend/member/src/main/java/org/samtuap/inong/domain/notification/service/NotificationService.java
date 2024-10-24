package org.samtuap.inong.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.samtuap.inong.common.exception.BaseCustomException;
import org.samtuap.inong.common.exceptionType.NotificationExceptionType;
import org.samtuap.inong.domain.notification.dto.NotificationGetResponse;
import org.samtuap.inong.domain.notification.entity.Notification;
import org.samtuap.inong.domain.notification.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.samtuap.inong.common.exceptionType.NotificationExceptionType.FORBIDDEN;
import static org.samtuap.inong.common.exceptionType.NotificationExceptionType.NOTIFICATION_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Page<NotificationGetResponse> getNotifications(Pageable pageable, boolean unread, Long memberId) {
        if(unread) {
            return notificationRepository.findAllByMemberIdAndIsRead(pageable, memberId, false).map(NotificationGetResponse::fromEntity);
        } else {
            return notificationRepository.findAllByMemberId(pageable, memberId).map(NotificationGetResponse::fromEntity);
        }
    }

    @Transactional
    public void readNotifications(Long memberId) {
        List<Notification> notifications = notificationRepository.findAllByMemberIdAndIsRead(memberId, false);
        for (Notification notification : notifications) {
            notification.updateIsRead(true);
        }
    }

    @Transactional
    public void readNotification(Long memberId, Long notificationId) {
        Notification noti = notificationRepository.findById(notificationId).orElseThrow(() -> new BaseCustomException(NOTIFICATION_NOT_FOUND));

        if(!noti.getMember().getId().equals(memberId)) {
            throw new BaseCustomException(FORBIDDEN);
        }

        noti.updateIsRead(true);
    }
}
