package org.samtuap.inong.domain.discount.api;

import lombok.RequiredArgsConstructor;
import org.samtuap.inong.domain.discount.dto.DiscountCreateRequest;
import org.samtuap.inong.domain.discount.dto.DiscountDetailResponse;
import org.samtuap.inong.domain.discount.dto.DiscountResponse;
import org.samtuap.inong.domain.discount.dto.DiscountUpdateRequest;
import org.samtuap.inong.domain.discount.service.DiscountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    // 할인 생성
    @PostMapping("/create")
    public void createDiscount(
            @RequestHeader("sellerId") Long sellerId, @RequestBody DiscountCreateRequest request) {
        discountService.createDiscount(request);
    }

    // 할인 수정
    @PutMapping("/{discountId}/update")
    public void updateDiscount(
            @PathVariable("discountId") Long discountId,
            @RequestHeader("sellerId") Long sellerId,
            @RequestBody DiscountUpdateRequest request) {
        discountService.updateDiscount(discountId, request);
    }

    // 할인 삭제
    @DeleteMapping("/{discountId}/delete")
    public void deleteDiscount(
            @PathVariable("discountId") Long discountId,
            @RequestHeader("sellerId") Long sellerId) {
        discountService.deleteDiscount(discountId);
    }

    // 할인 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<Page<DiscountResponse>> getDiscountList(
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(discountService.getDiscountList(pageable), HttpStatus.OK);
    }

    // 할인 디테일 조회
    @GetMapping("/{discountId}/detail")
    public ResponseEntity<DiscountDetailResponse> getDiscountDetail(@PathVariable("discountId") Long discountId) {
        return new ResponseEntity<>(discountService.getDiscountDetail(discountId), HttpStatus.OK);
    }
}
