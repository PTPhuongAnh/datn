package com.graduates.test.service;

import com.graduates.test.model.Book;
import com.graduates.test.dto.BookRespone;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {
    BookRespone createBook(Book book, List<MultipartFile> images) throws IOException;
    //BookRespone updateBook(Integer idBook, BookUpdate book);

    void deleteBook(String idBook);

    List<BookRespone> getAllBooks();

    Optional<BookRespone> getBookById(int idBook);

    String getBookUploadDir();

    Page<Book> getList(String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer quantity, Integer price, String category, String publisher, String distributor, int page, int sizes);

    Book updateBook(Integer id, String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer idCategory, Integer idPublisher, Integer idDistributor, Integer quantity, Integer price, List<MultipartFile> images) throws IOException;
}
