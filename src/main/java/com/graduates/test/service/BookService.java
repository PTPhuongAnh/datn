package com.graduates.test.service;

import com.graduates.test.model.Book;
import com.graduates.test.dto.BookRespone;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {
    BookRespone createBook(Book book, List<MultipartFile> images) throws IOException;
    //BookRespone updateBook(Integer idBook, BookUpdate book);

    void deleteBook(Integer idBook);
    List<BookRespone> getAllBooks();
    Optional<BookRespone> getBookById(int idBook);

    String getBookUploadDir();

    Page<Book> getList(String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer quantity, Integer price, String category, String publisher, String distributor, int page, int sizes);

    BookRespone updateBook(Integer idBook, String nameBook, String author, String descriptionShort, String descriptionLong, String size, String yearPublisher, String pageNumber, String barcode, Integer idCategory, Integer idPublisher, Integer idDistributor, Integer quantity, Integer price) throws IOException;

    List<Map<String, Object>> getBooksSortedBySales();

    //  List<Map<String, Object>> getBooksSortedBySales();
}
