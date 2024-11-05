package org.samtuap.inong.domain.notification.repository;

import org.samtuap.inong.domain.notification.entity.SellerFcmToken;
import org.samtuap.inong.domain.seller.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SellerFcmTokenRepository extends JpaRepository<SellerFcmToken, Long> {
    List<SellerFcmToken> findAllBySeller(Seller seller);

    Optional<SellerFcmToken> findByToken(String token);

    Optional<SellerFcmToken> findBySellerAndToken(Seller seller, String fcmToken);
}
