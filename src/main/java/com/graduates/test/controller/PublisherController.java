package com.graduates.test.controller;

import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.model.Publisher;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.resposity.PublisherResposity;
import com.graduates.test.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/publisher")
public class PublisherController {
    private PublisherService publisherService;
    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }


    @PostMapping
    public ResponseEntity<Object> createPublisherDetails(
            @RequestParam(value = "namePublisher", required = true) String namePublisher,
            @RequestParam(value = "addressPublisher", required = true) String addressPublisher
    ) {
        if (namePublisher == null || namePublisher.trim().isEmpty()) {
            return ResponseHandler.responeBuilder("Publisher name is required", HttpStatus.BAD_REQUEST, false, null);
        }
        if (addressPublisher == null || addressPublisher.trim().isEmpty()) {
            return ResponseHandler.responeBuilder("Publisher address is required", HttpStatus.BAD_REQUEST, false, null);
        }
        try {
            // Tạo đối tượng Publisher
            Publisher publisher = new Publisher();
            publisher.setNamePublisher(namePublisher);
            publisher.setAddressPublisher(addressPublisher);

            // Tạo nhà xuất bản mới
            String result = publisherService.createPublisher(publisher);

            // Trả về phản hồi thành công với thông điệp từ service
            return ResponseHandler.responeBuilder(result, HttpStatus.OK, true, null);

        } catch (Exception e) {
            // Xử lý các lỗi không dự đoán trước
            return ResponseHandler.responeBuilder("An error occurred while creating the publisher", HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }

    @PutMapping("/{idPublisher}")
    public ResponseEntity<Object> updatePublisherDetails(
            @PathVariable("idPublisher") Integer idPublisher,
            @RequestParam(value = "namePublisher", required = true) String namePublisher,
            @RequestParam(value = "addressPublisher", required = true) String addressPublisher
    ) {
        if (namePublisher == null || namePublisher.trim().isEmpty()) {
            return ResponseHandler.responeBuilder("Publisher name is required", HttpStatus.BAD_REQUEST, false, null);
        }
        if (addressPublisher == null || addressPublisher.trim().isEmpty()) {
            return ResponseHandler.responeBuilder("Publisher address is required", HttpStatus.BAD_REQUEST, false, null);
        }
        try {
            // Tìm kiếm và kiểm tra nhà xuất bản hiện tại
            Publisher existingPublisher = publisherService.findById(idPublisher);
            if (existingPublisher == null) {
                // Nhà xuất bản không tồn tại
                return ResponseHandler.responeBuilder("Publisher not found", HttpStatus.NOT_FOUND, false, null);
            }

            // Cập nhật các thông tin từ các tham số được cung cấp
            if (namePublisher != null) existingPublisher.setNamePublisher(namePublisher);
            if (addressPublisher != null) existingPublisher.setAddressPublisher(addressPublisher);

            // Gọi service để cập nhật nhà xuất bản
            String result = publisherService.updatePublisher(existingPublisher);

            // Trả về phản hồi thành công
            return ResponseHandler.responeBuilder(result, HttpStatus.OK, true, null);

        } catch (Exception e) {
            // Xử lý lỗi và trả về mã lỗi 500
            return ResponseHandler.responeBuilder("Error updating publisher", HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }

    @DeleteMapping("/{idPublisher}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer idPublisher) {
        try {
            publisherService.deletePublisher(idPublisher);
            return ResponseHandler.responeBuilder("publisher deleted successfully.",HttpStatus.OK,true,null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<Publisher>> getAllCategory() {
        return ResponseEntity.ok(publisherService.getAllPublisher());
    }

    @GetMapping("/{idPublisher}")
    public ResponseEntity<Object> getPublisherDetails(@PathVariable("idPublisher") Integer idPublisher) {
        try {
            Publisher publisher = publisherService.getPublisher(idPublisher);

            // Trả về thông tin nhà xuất bản nếu tìm thấy
            return ResponseHandler.responeBuilder("success", HttpStatus.OK, true, publisher);

        } catch (ResourceNotFoundException e) {
            // Xử lý trường hợp nhà xuất bản không tồn tại
            return ResponseHandler.responeBuilder(e.getMessage(), HttpStatus.NOT_FOUND, false, null);

        } catch (Exception e) {
            // Xử lý các lỗi không dự đoán trước
            return ResponseHandler.responeBuilder("An error occurred while retrieving the publisher", HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }
}