package com.graduates.test.resposity;

import com.graduates.test.model.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCategoryResposity extends JpaRepository<Book,Integer> {
//    @Query("SELECT b FROM Book b " +
//            "WHERE (:nameBook IS NULL OR b.nameBook LIKE %:nameBook%) " +
//            "AND (:author IS NULL OR b.author LIKE %:author%) " +
//            "AND (:category IS NULL OR b.category.nameCategory LIKE %:category%) " +
//            "AND (:publisher IS NULL OR b.publisher.namePublisher LIKE %:publisher%) "
//           )
//    List<Book> searchBooks(
//            @Param("nameBook") String nameBook,
//            @Param("author") String author,
//            @Param("category") String category,
//            @Param("publisher") String publisher
//            );
@Query("SELECT b FROM Book b " +
        "WHERE (:nameBook IS NULL OR b.nameBook LIKE %:nameBook%) " +
        "AND (:author IS NULL OR b.author LIKE %:author%) " +
        "AND (:category IS NULL OR b.category.nameCategory LIKE %:category%) " +
        "AND (:publisher IS NULL OR b.publisher.namePublisher LIKE %:publisher%)")
Page<Book> searchBooks(
        @Param("nameBook") String nameBook,
        @Param("author") String author,
        @Param("category") String category,
        @Param("publisher") String publisher,
        Pageable pageable);

    boolean existsByCategory_IdCategory(Integer categoryId);
    boolean existsByPublisher_IdPublisher(Integer publisherId);
}
