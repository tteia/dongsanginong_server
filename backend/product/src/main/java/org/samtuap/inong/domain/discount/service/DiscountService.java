package org.samtuap.inong.domain.discount.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.samtuap.inong.common.exception.BaseCustomException;
import org.samtuap.inong.domain.discount.dto.DiscountCreateRequest;
import org.samtuap.inong.domain.discount.dto.DiscountResponse;
import org.samtuap.inong.domain.discount.dto.DiscountUpdateRequest;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.samtuap.inong.domain.discount.repository.DiscountRepository;
import org.samtuap.inong.domain.product.entity.PackageProduct;
import org.samtuap.inong.domain.product.repository.PackageProductRepository;
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

    // 할인 생성
    @Transactional
    public void createDiscount(Long packageProductId, DiscountCreateRequest request) {
        PackageProduct packageProduct = packageProductRepository.findById(packageProductId)
                .orElseThrow(() -> new BaseCustomException(PRODUCT_NOT_FOUND));

        // packageProduct 중복 체크 로직 추가
        boolean discountExists = discountRepository.existsByPackageProduct(packageProduct);
        if (discountExists) {
            throw new BaseCustomException(DISCOUNT_ALREADY_EXISTS);
        }
        Discount discount = request.toEntity(packageProduct);

        // startAt이 현재 시간보다 뒤에 있으면 활성화 설정
        if (discount.getStartAt().isBefore(LocalDate.now())) {
            discount.updateDiscountActive(true);
        }
        discountRepository.save(discount);
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
        discountRepository.save(existingDiscount);
    }

    // 할인 삭제
    @Transactional
    public void deleteDiscount(Long discountId) {
        Discount existingDiscount = discountRepository.findById(discountId)
                .orElseThrow(() -> new BaseCustomException(DISCOUNT_NOT_FOUND));
        discountRepository.deleteById(existingDiscount.getId());
    }

    // 할인 리스트 조회
    public Page<DiscountResponse> getDiscountList(Pageable pageable) {
        Page<Discount> discountList = discountRepository.findAll(pageable);
        return discountList.map(DiscountResponse::fromEntity);
    }

    // 할인 디테일 조회
    public DiscountResponse getDiscountDetail(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new BaseCustomException(DISCOUNT_NOT_FOUND));
        return DiscountResponse.fromEntity(discount);
    }
}
