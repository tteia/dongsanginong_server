package org.samtuap.inong.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.samtuap.inong.common.client.OrderFeign;
import org.samtuap.inong.common.exception.BaseCustomException;
import org.samtuap.inong.domain.farm.entity.Farm;
import org.samtuap.inong.domain.farm.repository.FarmRepository;
import org.samtuap.inong.domain.notification.dto.KafkaNotificationRequest;
import org.samtuap.inong.domain.notification.dto.KafkaSellerNotificationRequest;
import org.samtuap.inong.domain.notification.dto.NotificationGetResponse;
import org.samtuap.inong.domain.notification.entity.SellerNotification;
import org.samtuap.inong.domain.notification.repository.SellerNotificationRepository;
import org.samtuap.inong.domain.seller.entity.Seller;
import org.samtuap.inong.domain.seller.repository.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.samtuap.inong.common.exceptionType.NotificationExceptionType.FORBIDDEN;
import static org.samtuap.inong.common.exceptionType.NotificationExceptionType.NOTIFICATION_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class NotificationService {
    private final SellerNotificationRepository sellerNotificationRepository;
    private final FarmRepository farmRepository;
    private final OrderFeign orderFeign;
    private final FcmService fcmService;

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

    // 매일 아침 7시 배송 알림
    public void notifyTodayDelivery() {
        List<Farm> farms = farmRepository.findAll();
        for (Farm farm : farms) {
            Long todayCount = orderFeign.getDeliveryCountByFarmId(farm.getId(), LocalDate.now().toString());
            Long tomorrowCount = orderFeign.getDeliveryCountByFarmId(farm.getId(), LocalDate.now().toString());

            if(todayCount == 0L || tomorrowCount == 0L) {
                continue;
            }

            String title = "오늘 또는 내일 배송할 상품이 있어요!";
            String content = "오늘: " + todayCount + "건, 내일: " + tomorrowCount + "건";
            String url = "/seller/delivery-management";
            fcmService.issueMessage(farm.getSellerId(), title, content, url);
        }
    }

    @Transactional
    public void readNotification(Long memberId, Long notificationId) {
        SellerNotification noti = sellerNotificationRepository.findById(notificationId).orElseThrow(() -> new BaseCustomException(NOTIFICATION_NOT_FOUND));

        if(!noti.getSeller().getId().equals(memberId)) {
            throw new BaseCustomException(FORBIDDEN);
        }

        noti.updateIsRead(true);
    }
}

