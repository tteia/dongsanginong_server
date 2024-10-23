package org.samtuap.inong.domain.farm.repository;

import org.samtuap.inong.common.exception.BaseCustomException;
import org.samtuap.inong.domain.farm.entity.FarmCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.samtuap.inong.common.exceptionType.ProductExceptionType.CATEGORY_NOT_FOUND;
import static org.samtuap.inong.common.exceptionType.ProductExceptionType.FARM_NOT_FOUND;

public interface FarmCategoryRepository extends JpaRepository<FarmCategory, Long> {
    default FarmCategory findByIdOrThrow(Long categoryId) {
        return findById(categoryId).orElseThrow(() -> new BaseCustomException(FARM_NOT_FOUND));
    }

    Optional<FarmCategory> findByTitle(String title);
    default FarmCategory findByTitleOrThrow(String title) {
        return findByTitle(title).orElseThrow(() -> new BaseCustomException(CATEGORY_NOT_FOUND));
    }
}
