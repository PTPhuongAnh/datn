package com.graduates.test.controller;

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

    public BookController(BookService bookService) {
        this.bookService = bookService;
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
    @PostMapping
    public ResponseEntity<?> addBookWithImages(
            @RequestParam(value = "nameBook",required = true) String nameBook,
            @RequestParam(value = "author",required = true) String author,
            @RequestParam(value = "description",required = true) String description,
            @RequestParam(value = "idCategory",required = false) Integer idCategory,
            @RequestParam(value = "idPublisher",required = false) Integer idPublisher,
            @RequestParam( "images") List<MultipartFile> images) {
        if (nameBook == null || nameBook.trim().isEmpty()) {
            return ResponseHandler.responeBuilder(
                    "Book name is required",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }
        if (author == null || author.trim().isEmpty()) {
            return ResponseHandler.responeBuilder(
                    "Author is required",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }
        if (description == null || description.trim().isEmpty()) {
            return ResponseHandler.responeBuilder(
                    "Description is required",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }
        if (idCategory == null) {
            return ResponseHandler.responeBuilder(
                    "Category ID is required",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }
        if (idPublisher == null) {
            return ResponseHandler.responeBuilder(
                    "Publisher ID is required",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }
        if (images == null || images.isEmpty()) {
            return ResponseHandler.responeBuilder(
                    "At least one image is required",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }

        // Khởi tạo đối tượng Book từ các tham số nhận được
        Book book = new Book();
        book.setNameBook(nameBook);
        book.setAuthor(author);
        book.setDescription(description);
        book.setCategory(new Category(idCategory)); // Khởi tạo đối tượng Category với id
        book.setPublisher(new Publisher(idPublisher)); // Khởi tạo đối tượng Publisher với id

        try {
            // Gọi service để lưu sách cùng với các ảnh//dòng này đang trả dữ liệu về

            //BookRespone savedBook = bookService.createBook(book, images);
            // nếu không cần trả dữ liệu về
            bookService.createBook(book, images);

            return ResponseHandler.responeBuilder(
//                    "Book added successfully with ID: " + savedBook.getIdBook(),
//                    HttpStatus.OK,
//                    true,
//                    savedBook
                    "Book added successfully ",
                    HttpStatus.OK,
                    true, null

            );
            // return ResponseEntity.ok("Book added successfully with ID: " + savedBook.getIdBook());
        } catch (IOException e) {
            return ResponseHandler.responeBuilder(
                    "Error adding book",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    false,
                    null
            );
            //  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding book");
        }
    }

@PutMapping("/{id}")
public ResponseEntity<?> updateBook(
        @PathVariable Integer id,
        @RequestParam( value = "nameBook", required = true) String nameBook,
        @RequestParam( value = "author",required = true) String author,
        @RequestParam( value = "description",required = true) String description,
        @RequestParam(value = "idCategory",required = false) Integer idCategory,
        @RequestParam(value = "idPublisher",required = false) Integer idPublisher,
        @RequestParam("images") List<MultipartFile> images) {
    if (nameBook == null || nameBook.trim().isEmpty()) {
        return ResponseHandler.responeBuilder(
                "Book name is required",
                HttpStatus.BAD_REQUEST,
                false,
                null
        );
    }
    if (author == null || author.trim().isEmpty()) {
        return ResponseHandler.responeBuilder(
                "Author is required",
                HttpStatus.BAD_REQUEST,
                false,
                null
        );
    }
    if (description == null || description.trim().isEmpty()) {
        return ResponseHandler.responeBuilder(
                "Description is required",
                HttpStatus.BAD_REQUEST,
                false,
                null
        );
    }
    if (idCategory == null) {
        return ResponseHandler.responeBuilder(
                "Category ID is required",
                HttpStatus.BAD_REQUEST,
                false,
                null
        );
    }
    if (idPublisher == null) {
        return ResponseHandler.responeBuilder(
                "Publisher ID is required",
                HttpStatus.BAD_REQUEST,
                false,
                null
        );
    }
    if (images == null || images.isEmpty()) {
        return ResponseHandler.responeBuilder(
                "At least one image is required",
                HttpStatus.BAD_REQUEST,
                false,
                null
        );
    }

    try {
        // Gọi service để cập nhật sách và ảnh
        Book updatedBook = bookService.updateBook(id, nameBook, author, description, idCategory, idPublisher, images);

        // Chuyển đổi thành BookResponse
        BookRespone response = convertToBookResponse(updatedBook);

       // return ResponseEntity.ok(response);
        return ResponseHandler.responeBuilder(
                "Book updated successfully",
                HttpStatus.OK,
                true,
                response
        );

    } catch (ResourceNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseHandler.responeBuilder(
                "Book not found",
                HttpStatus.NOT_FOUND,
                false,
                null
        );
    } catch (IOException e) {
     //   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        return ResponseHandler.responeBuilder(
                "Error updating book",
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
        response.setDescription(book.getDescription());
        response.setCategoryName(book.getCategory().getNameCategory());
       response.setPublisherName(book.getPublisher().getNamePublisher());
//        if (book.getCategory() != null) {
//            response.setCategoryName(book.getCategory().getNameCategory());
//        }
//
//        if (book.getPublisher() != null) {
//            response.setPublisherName(book.getPublisher().getNamePublisher());
//        }
        response.setImageUrls(imageUrls);
        return response;
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookRespone> getBookById(@PathVariable int id) {
        Optional<BookRespone> bookResponse = bookService.getBookById(id);
        return bookResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //   get danh sách
//    @GetMapping("/list")
//    public ResponseEntity<?> getList(
//            @RequestParam(value = "nameBook", required = false) String nameBook,
//            @RequestParam(value = "author", required = false) String author,
//            @RequestParam(value = "category", required = false) String category,
//            @RequestParam(value = "publisher", required = false) String publisher) {
//
//        List<BookRespone> books = bookService.getList(nameBook, author, category, publisher);
//
//        if (books.isEmpty()) {
//            return ResponseHandler.responeBuilder("No books found", HttpStatus.OK, false, null);
//            //  return ResponseEntity.ok(Collections.singletonMap("status", false));
//            //   return ResponseEntity.ok(new BookRespone(false)); // Trả về status = false nếu không tìm thấy sách
//        } else {
////            books.forEach(book -> book.setStatus(true));
////            return ResponseEntity.ok(books); // Trả về danh sách sách với status = true
//
//            return ResponseHandler.responeBuilder("Books found", HttpStatus.OK, true, books);
//        }
//
//    }


    @GetMapping("/list")
    public ResponseEntity<?> getList(
            @RequestParam(value = "nameBook", required = false) String nameBook,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Book> bookPage = bookService.getList(nameBook, author, category, publisher, page, size);

        if (bookPage.isEmpty()) {
            return ResponseHandler.responeBuilder("No books found", HttpStatus.OK, false, null);
        } else {
//            List<BookRespone> bookResponses = bookPage.getContent().stream()
//                    .map(bookService::convertToBookResponse)
//                    .collect(Collectors.toList());
            List<BookRespone> bookResponses = bookPage.getContent().stream()
                    .map(book -> convertToBookResponse(book))
                    .collect(Collectors.toList());
            Map<String, Object> response = new HashMap<>();
            response.put("books", bookResponses);
            response.put("currentPage", bookPage.getNumber());
            response.put("totalItems", bookPage.getTotalElements());
            response.put("totalPages", bookPage.getTotalPages());

            return ResponseHandler.responeBuilder("Books found", HttpStatus.OK, true, response);
        }
    }
    }




