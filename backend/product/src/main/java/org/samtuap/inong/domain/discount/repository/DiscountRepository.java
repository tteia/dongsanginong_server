package org.samtuap.inong.domain.discount.repository;

import org.samtuap.inong.common.exception.BaseCustomException;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

import static org.samtuap.inong.common.exceptionType.ProductExceptionType.DISCOUNT_NOT_FOUND;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findAllByStartAt(LocalDate now);

    List<Discount> findAllByEndAtBefore(LocalDate now);

    List<Discount> findAllByStartAtLessThanEqualAndDiscountActiveFalse(LocalDate now);

    List<Discount> findAllByEndAtLessThanEqualAndDiscountActiveTrue(LocalDate now);

    List<Discount> findAll();

    default Discount findByIdThrow(Long id) {
        return findById(id).orElseThrow(() -> new BaseCustomException(DISCOUNT_NOT_FOUND));
    }
}
