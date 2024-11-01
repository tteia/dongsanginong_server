package org.samtuap.inong.domain.discount.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.samtuap.inong.common.exception.BaseCustomException;
import org.samtuap.inong.domain.discount.dto.DiscountCreateRequest;
import org.samtuap.inong.domain.discount.dto.DiscountDetailResponse;
import org.samtuap.inong.domain.discount.dto.DiscountResponse;
import org.samtuap.inong.domain.discount.dto.DiscountUpdateRequest;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.samtuap.inong.domain.discount.repository.DiscountRepository;
import org.samtuap.inong.domain.product.entity.PackageProduct;
import org.samtuap.inong.domain.product.repository.PackageProductRepository;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.samtuap.inong.common.exceptionType.ProductExceptionType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final PackageProductRepository packageProductRepository;
    private final CacheManager cacheManager;

    // 할인 생성 => seller가 할인 등록
    @Transactional
    public void createDiscount(DiscountCreateRequest request) {
        // discount 엔티티에 할인 등록
        Discount discount = request.toEntity();

        // startAt이 현재 시간보다 뒤에 있으면 활성화 설정
        if (discount.getStartAt().isBefore(LocalDate.now())) {
            discount.updateDiscountActive(true);
        }
        discountRepository.save(discount);

        // packageProduct 엔티티에 discount_id 등록
        for (Long productId : request.productIdList()) {
            PackageProduct product = packageProductRepository.findByIdOrThrow(productId);
            if (product.getDiscountId() == null) { // product의 discountid가 null이면 -> 받아온 discountid 넣기
                product.updateDiscountId(discount.getId());
            } else { // null이 아니면 -> 등록할 수 없다고 예외처리
                throw new BaseCustomException(DISCOUNT_ALREADY_EXISTS);
            }
            cacheManager.getCache("PackageDetail").evict(productId);
        }
    }

    // 할인 수정
    @Transactional
    public void updateDiscount(Long discountId, DiscountUpdateRequest request) {
        Discount existingDiscount = discountRepository.findById(discountId)
                .orElseThrow(() -> new BaseCustomException(DISCOUNT_NOT_FOUND));
        request.updateEntity(existingDiscount);

        // startAt이 현재 시간보다 뒤에 있거나 오늘 날짜로 설정하면 활성화
        if (existingDiscount.getStartAt().isBefore(LocalDate.now())
                || existingDiscount.getStartAt().equals(LocalDate.now())) {
            log.info(">>>>날짜 : " + existingDiscount.getStartAt() + " >>>> 오늘날짜거나 이전 => 활성화");
            existingDiscount.updateDiscountActive(true);
        } else {
            log.info(">>>>날짜 : " + existingDiscount.getStartAt() + " >>>> 오늘보다 이후 => 비활성화");
            existingDiscount.updateDiscountActive(false);
        }

        // 기존에 할인에 적용된 상품들의 ID 리스트
        List<PackageProduct> existingProducts = packageProductRepository.findAllByDiscountId(discountId);
        List<Long> existingProductIds = existingProducts.stream()
                .map(PackageProduct::getId)
                .collect(Collectors.toList());

        // 요청으로 받은 새로운 상품 ID 리스트
        List<Long> newProductIds = request.productIdList();

        // 할인에서 제거할 상품들 (기존에 있었지만 새 리스트에는 없는 상품들)
        List<Long> productsToRemoveDiscount = existingProductIds.stream()
                .filter(id -> !newProductIds.contains(id))
                .collect(Collectors.toList());
        for (Long productId : productsToRemoveDiscount) {
            PackageProduct product = packageProductRepository.findByIdOrThrow(productId);
            product.updateDiscountId(null);
            cacheManager.getCache("PackageDetail").evict(productId);
        }

        // 할인에 새로 추가할 상품들 (새 리스트에는 있지만 기존에 없었던 상품들)
        List<Long> productsToAddDiscount = newProductIds.stream()
                .filter(id -> !existingProductIds.contains(id))
                .collect(Collectors.toList());
        for (Long productId : productsToAddDiscount) {
            PackageProduct product = packageProductRepository.findByIdOrThrow(productId);
            if (product.getDiscountId() == null) {
                product.updateDiscountId(discountId);
            } else {
                throw new BaseCustomException(DISCOUNT_ALREADY_EXISTS);
            }
            cacheManager.getCache("PackageDetail").evict(productId);
        }

        discountRepository.save(existingDiscount);
    }

    // 할인 삭제 => 할인을 삭제하면 product 엔티티에서 discount_id의 상품을 모두 찾아 null로 변경해주고 discount에선 deleted_at 추가
    @Transactional
    public void deleteDiscount(Long discountId) {
        Discount existingDiscount = discountRepository.findById(discountId)
                .orElseThrow(() -> new BaseCustomException(DISCOUNT_NOT_FOUND));

        // product 엔티티에서 discount_id가 적용된 상품 목록 찾아오기 => discount_id 모두 null로 설정
        List<PackageProduct> productList = packageProductRepository.findAllByDiscountId(existingDiscount.getId());
        for (PackageProduct product : productList) {
            product.updateDiscountId(null);
            cacheManager.getCache("PackageDetail").evict(product.getId());
        }
        discountRepository.deleteById(existingDiscount.getId());
    }

    // 할인 리스트 조회
    public Page<DiscountResponse> getDiscountList(Pageable pageable) {
        Page<Discount> discountList = discountRepository.findAll(pageable);
        return discountList.map(DiscountResponse::fromEntity);
    }

    // 할인 디테일 조회
    public DiscountDetailResponse getDiscountDetail(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new BaseCustomException(DISCOUNT_NOT_FOUND));

        // 해당 할인에 적용된 상품들의 ID 리스트 조회
        List<PackageProduct> productList = packageProductRepository.findAllByDiscountId(discountId);
        List<Long> productIdList = productList.stream()
                .map(PackageProduct::getId)
                .collect(Collectors.toList());

        return DiscountDetailResponse.fromEntity(discount, productIdList);
    }
}
