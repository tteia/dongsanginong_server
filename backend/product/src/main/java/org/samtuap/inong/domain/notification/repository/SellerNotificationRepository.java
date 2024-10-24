package org.samtuap.inong.domain.notification.repository;

import org.samtuap.inong.domain.notification.entity.SellerNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerNotificationRepository extends JpaRepository<SellerNotification, Long> {
    Page<SellerNotification> findAllBySellerId(Pageable pageable, Long sellerId);

    Page<SellerNotification> findAllBySellerIdAndIsRead(Pageable pageable, Long sellerId, boolean isRead);

    List<SellerNotification> findAllBySellerIdAndIsRead(Long sellerId, boolean isRead);
}
