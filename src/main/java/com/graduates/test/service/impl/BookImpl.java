package com.graduates.test.service.impl;

import com.graduates.test.dto.BookRespone;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.*;
import com.graduates.test.resposity.BookCategoryResposity;
import com.graduates.test.resposity.CategoryResposity;
import com.graduates.test.resposity.ImageResposity;
import com.graduates.test.resposity.PublisherResposity;
import com.graduates.test.service.BookService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookImpl implements BookService {

    @Value("${file.upload-dirs}")
    private String bookUploadDir;



    @Autowired
    private BookCategoryResposity bookCategoryResposity;

    private CategoryResposity categoryRepository;

    private PublisherResposity publisherRepository;



    private ImageResposity imageResposity;


 public BookImpl(BookCategoryResposity bookCategoryResposity, CategoryResposity categoryRepository, PublisherResposity publisherRepository, ImageResposity imageResposity) {
     this.bookCategoryResposity = bookCategoryResposity;
     this.categoryRepository = categoryRepository;
     this.publisherRepository = publisherRepository;
     this.imageResposity = imageResposity;
 }

    public String getBookUploadDir() {
        return bookUploadDir;
    }



    @Override
    public void deleteBook(String idBook) {

    }

    @Override
    public List<BookRespone> getAllBooks() {
        return null;
    }


    public BookRespone createBook(Book book, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty()) {
            throw new IOException("At least one image is required");
        }
        Book savedBook = bookCategoryResposity.save(book);
        saveBookImages(savedBook, images);
        return convertToBookResponse(savedBook);
    }



    public Optional<BookRespone> getBookById(int id) {
        return bookCategoryResposity.findById(id)
                .map(this::convertToBookResponse);
    }

    private BookRespone convertToBookResponse(Book book) {
        String baseUrl = "http://localhost:8080/book/image/";

        List<String> imageUrls = book.getImageBooks().stream()
                .map(image -> baseUrl + encodeURIComponent(image.getImage_url()))
                .collect(Collectors.toList());

        BookRespone response = new BookRespone();
        response.setIdBook(book.getIdBook());
        response.setNameBook(book.getNameBook());
        response.setAuthor(book.getAuthor());
        response.setDescription(book.getDescription());
        response.setCategoryName(book.getCategory().getNameCategory());
        response.setPublisherName(book.getPublisher().getNamePublisher());
        response.setQuantity(book.getQuantity());
        response.setPrice(book.getPrice());
        response.setImageUrls(imageUrls);


        return response;
    }

    // Mã hóa URL
    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode URL component", e);
        }
    }

public Page<Book> getList(String nameBook, String author, String category, String publisher, Integer quantity, Integer price, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return bookCategoryResposity.searchBooks(nameBook, author, category, publisher, pageable);
}

//    @Override
//    public Book updateBook(Integer id, String nameBook, String author, String description, Integer quantity, Integer price, Integer categoryId, Integer publisherId, List<MultipartFile> images) throws IOException {
//        return null;
//    }

    private void deleteOldImages(Book book) {
    List<ImageBook> oldImages = imageResposity.findByBook(book);
    for (ImageBook image : oldImages) {
        File file = new File(bookUploadDir + File.separator + image.getImage_url());
        if (file.exists()) {
            file.delete(); // Xóa tệp ảnh khỏi hệ thống
        }
        imageResposity.delete(image); // Xóa thông tin ảnh trong cơ sở dữ liệu
    }
}

private void updateBookImages(Book book, List<MultipartFile> newImages) throws IOException {
    // Xóa ảnh cũ
    deleteOldImages(book);

    // Lưu ảnh mới
    saveBookImages(book, newImages);
}

//
public Book updateBook(Integer id, String nameBook, String author, String description, Integer categoryId, Integer publisherId,Integer quantity, Integer price, List<MultipartFile> images) throws ResourceNotFoundException, IOException {
    // Tìm sách theo ID
    Book book = bookCategoryResposity.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + id));

    // Cập nhật thông tin sách
    if (nameBook != null) book.setNameBook(nameBook);
    if (author != null) book.setAuthor(author);
    if (description != null) book.setDescription(description);
    if (categoryId != null) book.setCategory(new Category(categoryId)); // Gán thể loại
    if (publisherId != null) book.setPublisher(new Publisher(publisherId)); // Gán nhà xuất bảnif
    if(quantity !=null) book.setQuantity(quantity);
    if(price !=null) book.setQuantity(price);
    // Cập nhật ảnh sách nếu có
    if (images != null && !images.isEmpty()) {
        updateBookImages(book, images);
    }else {
        throw new IOException("At least one image is required");
    }

    // Lưu sách đã cập nhật vào cơ sở dữ liệu
    return bookCategoryResposity.save(book);
}

    private void saveBookImages(Book savedBook, List<MultipartFile> images) throws IOException {
        File uploadDir = new File(bookUploadDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        for (MultipartFile image : images) {
            try {


                // Sử dụng UUID để tạo tên tệp duy nhất
                String originalFileName = image.getOriginalFilename();
                String fileName = originalFileName;
                String filePath = uploadDir.getAbsolutePath() + File.separator + fileName;
                File file = new File(filePath);

                // Lưu tệp ảnh vào thư mục
                image.transferTo(file);

                // Lưu thông tin ảnh vào cơ sở dữ liệu, chỉ lưu tên tệp
                ImageBook imageBook = new ImageBook();
                imageBook.setImage_url(fileName); // Chỉ lưu tên tệp
                imageBook.setBook(savedBook);

                imageResposity.save(imageBook); // Lưu thông tin ảnh vào database
            }catch (IOException e){
                System.err.println("Error saving image: " + e.getMessage());
                throw e;
            }
        }
    }

}