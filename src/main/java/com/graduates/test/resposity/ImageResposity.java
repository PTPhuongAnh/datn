package com.graduates.test.resposity;

import com.graduates.test.model.Book;
import com.graduates.test.model.ImageBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ImageResposity extends JpaRepository<ImageBook,Integer> {
    void deleteAllByBook(Book book);

    void deleteByBook(Book book);

    List<ImageBook> findByBook(Book book);

    @Query("SELECT ib.image_url FROM ImageBook ib WHERE ib.book.idBook = :bookId")
    List<String> findImageUrlsByBookId(Integer bookId);
}
