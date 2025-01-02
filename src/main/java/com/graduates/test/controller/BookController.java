package com.graduates.test.controller;

import com.graduates.test.Config.JwtService;
import com.graduates.test.dto.BookRespone;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.*;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private JwtService jwtService;

    public BookController(BookService bookService, JwtService jwtService) {
        this.bookService = bookService;
        this.jwtService = jwtService;
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        Path path = Paths.get(bookService.getBookUploadDir()).resolve(decodedFileName);

        if (Files.exists(path)) {
            try {
                byte[] fileData = Files.readAllBytes(path);
                String mimeType = Files.probeContentType(path);
                MediaType mediaType = MediaType.parseMediaType(mimeType != null ? mimeType : MediaType.APPLICATION_OCTET_STREAM_VALUE);

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(fileData);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //thêm sách
    @PostMapping("/create")
    public ResponseEntity<?> addBookWithImages(
            @RequestParam(value = "nameBook",required = true) String nameBook,
            @RequestParam(value = "author",required = true) String author,
            @RequestParam(value = "description_short",required = false) String description_short,
            @RequestParam(value = "description_long",required = false) String description_long,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "year_publisher", required = false) String year_publisher,
            @RequestParam(value = "page_number",required = false) String page_number,
            @RequestParam(value = "barcode",required = false)String barcode,
            @RequestParam(value = "idCategory",required = false) Integer idCategory,
            @RequestParam(value = "idPublisher",required = false) Integer idPublisher,
            @RequestParam(value = "idDistributor",required = false) Integer idDistributor,
            @RequestParam(value="quantity", required = false) Integer quantity,
            @RequestParam(value = "price", required = false) Integer price,
            @RequestParam(value = "images", required = true) List<MultipartFile> images,
            @RequestHeader("Authorization") String token) {
        token = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        System.out.println("username "+ username);

  //      bookService.validateBookInputs(nameBook, author, description, idCategory, idPublisher, quantity, price, images);

        // Khởi tạo đối tượng Book từ các tham số nhận được
        Book book = new Book();
        book.setNameBook(nameBook);
        book.setAuthor(author);
      //
        book.setDescription_short(description_short);
        book.setDescription_long(description_long);
        book.setSize(size);
        book.setYear_publisher(year_publisher);
        book.setPage_number(page_number);
        book.setBarcode(barcode);
        book.setCategory(new Category(idCategory)); // Khởi tạo đối tượng Category với id
        book.setPublisher(new Publisher(idPublisher)); // Khởi tạo đối tượng Publisher với id
        book.setDistributor(new Distributor(idDistributor));
        book.setQuantity(quantity);
        book.setPrice(price);
        book.setCreatedBy(username);
        book.setUpdatedBy(username);
        try {
            // Gọi service để lưu sách cùng với các ảnh//dòng này đang trả dữ liệu về

            //BookRespone savedBook = bookService.createBook(book, images);
            // nếu không cần trả dữ liệu về
            bookService.createBook(book, images);

            return ResponseHandler.responeBuilder(
                    HttpStatus.OK,
                    true, null

            );
        } catch (IOException e) {
            return ResponseHandler.responeBuilder1(
                    HttpStatus.OK,
                    false,
                    null
            );

        }
    }


@PutMapping("/{idBook}")
public ResponseEntity<?> updateBook(
        @PathVariable("idBook") Integer idBook,
        @RequestParam(value = "nameBook") String nameBook,
        @RequestParam(value = "author") String author,
        @RequestParam(value = "description_short") String description_short,
        @RequestParam(value = "description_long") String description_long,
        @RequestParam(value = "size") String size,
        @RequestParam(value = "year_publisher") String year_publisher,
        @RequestParam(value = "page_number") String page_number,
        @RequestParam(value = "barcode") String barcode,
        @RequestParam(value = "idCategory") Integer idCategory,
        @RequestParam(value = "idPublisher") Integer idPublisher,
        @RequestParam(value = "idDistributor") Integer idDistributor,
        @RequestParam(value = "quantity") Integer quantity,
        @RequestParam(value = "price") Integer price,
//        @RequestParam(value = "images", required = false) List<MultipartFile> images,
        @RequestHeader("Authorization") String token) {

    // Gọi service để cập nhật sách và ảnh
    try {
        token = token.replace("Bearer ", "");
        String username = jwtService.extractUsername(token);
        System.out.println("username "+ username);
        // Có thể thêm kiểm tra đầu vào ở đây
        // bookService.validateBookInputs(nameBook, author, description_short, idCategory, idPublisher, quantity, price, images);

        BookRespone updatedBook = bookService.updateBook(idBook, nameBook, author, description_short,
                description_long, size, year_publisher, page_number, barcode, idCategory, idPublisher,
                idDistributor, quantity, price);

        System.out.println("Updating book with ID: " + idBook);
       // System.out.println("Images: " + images.size());
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: " + price);

        return ResponseHandler.responeBuilder(
                HttpStatus.OK,
                true,
                updatedBook
        );

    } catch (ResourceNotFoundException e) {
        System.out.println("Error: " + e.getMessage());
        return ResponseHandler.responeBuilder(
                HttpStatus.NOT_FOUND,
                false,
                null
        );
    } catch (Exception e) {
        System.out.println("Unexpected error: " + e.getMessage());
        return ResponseHandler.responeBuilder(
                HttpStatus.INTERNAL_SERVER_ERROR,
                false,
                null
        );
    }
}
    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to encode URL component", e);
        }
    }


    // Phương thức hỗ trợ để chuyển Book sang BookResponse

    // Hàm chuyển đổi từ Book sang BookResponse
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


    @GetMapping("/{id}")
    public ResponseEntity<BookRespone> getBookById(@PathVariable int id) {
        Optional<BookRespone> bookResponse = bookService.getBookById(id);
        return bookResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/list")
    public ResponseEntity<?> getList(
            @RequestParam(value = "nameBook", required = false) String nameBook,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "description_short",required = false) String description_short,
            @RequestParam(value = "description_long",required = false) String description_long,
            @RequestParam(value = "size", required = false) String size,
            @RequestParam(value = "year_publisher", required = false) String year_publisher,
            @RequestParam(value = "page_number",required = false) String page_number,
            @RequestParam(value = "barcode",required = false)String barcode,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "distributor", required = false) String distributor,
            @RequestParam(value = "quantity" ,required = false)Integer quantity,
            @RequestParam(value = "price",required = false) Integer price,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sizes", defaultValue = "10") int sizes) {

        Page<Book> bookPage = bookService.getList( nameBook,  author,  description_short,  description_long,  size,  year_publisher,  page_number,  barcode,  quantity, price,  category,  publisher,  distributor,  page, sizes);

        if (bookPage.isEmpty()) {
            return ResponseHandler.responeBuilder( HttpStatus.OK, false, null);
        } else {

            List<BookRespone> bookResponses = bookPage.getContent().stream()
                    .map(book -> convertToBookResponse(book))
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("books", bookResponses);
            response.put("currentPage", bookPage.getNumber());
            response.put("totalItems", bookPage.getTotalElements());
            response.put("totalPages", bookPage.getTotalPages());

            return ResponseHandler.responeBuilder( HttpStatus.OK, true, response);
        }
    }


    @DeleteMapping("/delete/{idBook}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer idBook) {
        try {
            bookService.deleteBook(idBook);
            return ResponseHandler.responeBuilder(HttpStatus.OK,true,"book deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseHandler.responeBuilder1(HttpStatus.OK,false,e.getMessage());
        }
    }
    @GetMapping("/books-sales")
    public ResponseEntity<?> getBooksSales() {
        List<Map<String, Object>> bookSales = bookService.getBooksSortedBySales();
        return ResponseHandler.responeBuilder(HttpStatus.OK,true,bookSales);
    }
    }




