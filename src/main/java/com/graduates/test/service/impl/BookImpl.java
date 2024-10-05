package com.graduates.test.service.impl;

import com.graduates.test.dto.BookRespone;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.*;
import com.graduates.test.resposity.*;
import com.graduates.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
    private DistributorResposity distributorResposity;



    private ImageResposity imageResposity;

    public BookImpl(BookCategoryResposity bookCategoryResposity, CategoryResposity categoryRepository, PublisherResposity publisherRepository, DistributorResposity distributorResposity, ImageResposity imageResposity) {
        this.bookCategoryResposity = bookCategoryResposity;
        this.categoryRepository = categoryRepository;
        this.publisherRepository = publisherRepository;
        this.distributorResposity = distributorResposity;
        this.imageResposity = imageResposity;
    }
// public BookImpl(BookCategoryResposity bookCategoryResposity, CategoryResposity categoryRepository, PublisherResposity publisherRepository, ImageResposity imageResposity) {
//     this.bookCategoryResposity = bookCategoryResposity;
//     this.categoryRepository = categoryRepository;
//     this.publisherRepository = publisherRepository;
//     this.imageResposity = imageResposity;
// }

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
        response.setDescription_short(book.getDescription_short());
        response.setDescription_long(book.getDescription_long());
        response.setSize(book.getSize());
        response.setYear_publisher(book.getYear_publisher());
        response.setPage_number(book.getPage_number());
        response.setBarcode(book.getBarcode());
        response.setCategoryName(book.getCategory().getNameCategory());
        response.setPublisherName(book.getPublisher().getNamePublisher());
        response.setDistributorName(book.getDistributor().getNameDistributor());
        response.setQuantity(book.getQuantity());
        response.setPrice(book.getPrice());
        response.setCreateAt(book.getCreateAt());
        response.setUpdateAt(book.getUpdateAt());
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

public Page<Book> getList(String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer quantity,Integer price, String category, String publisher, String distributor, int page, int sizes) {
    Pageable pageable = PageRequest.of(page, sizes);
    return bookCategoryResposity.searchBooks(nameBook, author, category, publisher, distributor,pageable);
}

//    @Override
//    public Book updateBook(Integer id, String nameBook, String author, String description_short, String description_long, String size, String year_publisher, String page_number, String barcode, Integer idCategory, Integer idPublisher, Integer idDistributor, Integer quantity, Integer price, List<MultipartFile> images) {
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


    public BookRespone updateBook(Integer idBook, String nameBook, String author, String description_short,
                                  String description_long, String size, String year_publisher, String page_number,
                                  String barcode, Integer categoryId,
                                  Integer publisherId, Integer distributorId,Integer quantity, Integer price, List<MultipartFile> images)
            throws ResourceNotFoundException, IOException {
        // Tìm sách theo ID
        Book book = bookCategoryResposity.findById(idBook)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với ID: " + idBook));

        // Cập nhật thông tin sách
        if (nameBook != null) book.setNameBook(nameBook);
        if (author != null) book.setAuthor(author);
        if (description_short != null) book.setDescription_short(description_short);
        if (description_long != null) book.setDescription_long(description_long);
        if (size != null) book.setSize(size);
        if (year_publisher != null) book.setYear_publisher(year_publisher);
        if (page_number != null) book.setPage_number(page_number);
        if (barcode != null) book.setBarcode(barcode);

        // Cập nhật category, publisher, và distributor
        if (categoryId != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            category.ifPresentOrElse(book::setCategory,
                    () -> { throw new ResourceNotFoundException("Không tìm thấy thể loại với ID: " + categoryId); });
        }

        if (publisherId != null) {
            Optional<Publisher> publisher = publisherRepository.findById(publisherId);
            publisher.ifPresentOrElse(book::setPublisher,
                    () -> { throw new ResourceNotFoundException("Không tìm thấy nhà xuất với ID: " + publisherId); });
        }

        if (distributorId != null) {
            Optional<Distributor> distributor = distributorResposity.findById(distributorId);
            distributor.ifPresentOrElse(book::setDistributor,
                    () -> { throw new ResourceNotFoundException("Không tìm thấy nhà phân phối với ID: " + distributorId); });
        }

        // Cập nhật số lượng và giá
        if (quantity != null) book.setQuantity(quantity);
        if (price != null) book.setPrice(price);

        // Cập nhật ảnh sách nếu có
        if (images != null && !images.isEmpty()) {
            updateBookImages(book, images);
        } else {
            throw new IOException("At least one image is required");
        }

        // Lưu sách đã cập nhật vào cơ sở dữ liệu
        Book updatedBook = bookCategoryResposity.save(book);

        // Chuyển đổi sang BookRespone và trả về
        return convertToBookResponse(updatedBook);
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


    public void validateBookInputs(String nameBook, String author, String description, Integer idCategory,
                                   Integer idPublisher, Integer quantity, Integer price, List<MultipartFile> images) {
        if (nameBook == null || nameBook.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book name is required");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author is required");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        }
        if (idCategory == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category ID is required");
        }
        if (idPublisher == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Publisher ID is required");
        }
        if (quantity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity is required");
        }
        if (price == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price is required");
        }
        if (images == null || images.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one image is required");
        }
    }

}