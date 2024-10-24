package org.samtuap.inong.domain.delivery.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.samtuap.inong.domain.delivery.dto.*;
import org.samtuap.inong.domain.delivery.service.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;



@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 사장님 페이지 > 다가오는 배송 처리
     */
    @GetMapping("/upcoming/list")
    public ResponseEntity<Page<DeliveryUpComingListResponse>> upcomingDelivery(
            @RequestHeader("sellerId") Long sellerId,
            @PageableDefault(size = 5, sort = "deliveryDueDate", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DeliveryUpComingListResponse> list = deliveryService.upcomingDelivery(sellerId, pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 사장님 페이지 > 운송장 번호를 등록하면 delivery 엔티티의 status가 IN_DELIVERY로 변경
     */
    @PatchMapping("/createBillingNumber/{id}")
    public ResponseEntity<?> createBillingNumber(@PathVariable("id") Long id,
                                                 @RequestBody BillingNumberCreateRequest dto) {
        deliveryService.createBillingNumber(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 사장님 페이지 > 처리한 배송 조회
     * 정렬) IN_DELIVERY가 먼저 출력되고 그다음 AFTER_DELIVERY가 출력되도록 함 > 그 안에서는 배송날짜로 DESC 정렬
     */
    @GetMapping("/completed/list")
    public ResponseEntity<Page<DeliveryCompletedListResponse>> completedDelivery(
            @RequestHeader("sellerId") Long sellerId,
            @PageableDefault(size = 5, sort = {"deliveryStatus", "deliveryAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DeliveryCompletedListResponse> list = deliveryService.completedDelivery(sellerId, pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<DeliveryListResponse>> getOrderDeliveryList(@PageableDefault(size = 8, sort = {"deliveryAt"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                                           @RequestHeader("myId") Long memberId) {
        Page<DeliveryListResponse> myOrderDeliveryList = deliveryService.getOrderDeliveryList(pageable, memberId);
        return new ResponseEntity<>(myOrderDeliveryList, HttpStatus.OK);
    }

    // feign 요청 용
    @GetMapping("/farm/{farmId}/count")
    public Long getUpcomingDeliveryCount(@PathVariable("farmId") Long farmId,
                                         @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return deliveryService.getDeliveryCount(farmId, date);
    }
}