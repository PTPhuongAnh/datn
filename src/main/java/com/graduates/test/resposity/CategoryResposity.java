package com.graduates.test.resposity;

import com.graduates.test.model.Book;
import com.graduates.test.model.Category;
import com.graduates.test.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CategoryResposity extends JpaRepository <Category,Integer>{
//    List<Category> findByDeletedFalse();
//List<Category> findByFlag_deleteFalse();
List<Category> findByDeletedFalse();
    Page<Category> findByNameCategoryContaining(String nameCategory, Pageable pageable);
    Page<Category> findAllByDeletedFalse(Pageable pageable);
//    @Query("SELECT b FROM Category b " +
//            "WHERE (:nameCategory IS NULL OR b.nameCategory LIKE %:nameCategory%) "
//            )
//    Page<Category> searchCategory(
//            @Param("nameCategory") String nameCategory,
//            Pageable pageable);

    @Query("SELECT o FROM Category o WHERE (:categoryCode IS NULL OR o.categoryCode LIKE %:categoryCode%) " +
            "AND (:nameCategory IS NULL OR o.nameCategory LIKE %:nameCategory%)"+
            "AND (:startDate IS NULL OR o.createAt >= :startDate) " +
            "AND (:endDate IS NULL OR o.createAt <= :endDate)")
    Page<Category> findCategoryWithSearch(@Param("nameCategory") String categoryName,
                                      @Param("categoryCode") String categoryCode,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     Pageable pageable);

    boolean existsByCategoryCode(String categoryCode);
}
