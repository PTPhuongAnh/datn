package com.graduates.test.controller;

import com.graduates.test.dto.DistributorRespone;
import com.graduates.test.dto.PublisherRespone;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.model.Distributor;
import com.graduates.test.model.Publisher;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.resposity.PublisherResposity;
import com.graduates.test.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/publisher")
public class PublisherController {
    private PublisherService publisherService;
    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createPublisherDetails(
            @RequestParam(value = "namePublisher", required = true) String namePublisher,
            @RequestParam(value = "addressPublisher", required = true) String addressPublisher,
            @RequestParam(value = "phonePublisher", required = true) String phonePublisher,
            @RequestParam(value = "emailPublisher", required = true) String emailPublisher

    ) {

        try {
            // Tạo đối tượng Publisher
            Publisher publisher = new Publisher();
            publisher.setNamePublisher(namePublisher);
            publisher.setAddressPublisher(addressPublisher);
            publisher.setEmailPublisher(emailPublisher);
            publisher.setPhonePublisher(phonePublisher);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updatePublisherDetails(
            @PathVariable("idPublisher") Integer idPublisher,
            @RequestParam(value = "namePublisher", required = true) String namePublisher,
            @RequestParam(value = "addressPublisher", required = true) String addressPublisher,
            @RequestParam(value = "phonePublisher", required = true) String phonePublisher,
            @RequestParam(value = "emailPublisher", required = true) String emailPublisher
    ) {

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
            if(phonePublisher!=null) existingPublisher.setPhonePublisher(phonePublisher);
            if(emailPublisher!=null) existingPublisher.setEmailPublisher(emailPublisher);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePublisher(@PathVariable Integer idPublisher) {
        try {
            publisherService.markPublisherAsDeleted(idPublisher);
            return ResponseHandler.responeBuilder("publisher deleted successfully.",HttpStatus.OK,true,null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
//    @DeleteMapping("/{idDistributor}")
//    public ResponseEntity<?> deleteDistributor(@PathVariable Integer idDistributor) {
//        try {
//            distributorService.markDistributorAsDeleted(idDistributor);
//            return ResponseHandler.responeBuilder("publisher deleted successfully.",HttpStatus.OK,true,null);
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

//    @GetMapping("/list")
//    public ResponseEntity<List<Publisher>> getAllCategory() {
//        return ResponseEntity.ok(publisherService.getAllPublisher());
//    }

    @GetMapping("/list")
    public ResponseEntity<?> getPublisherList(
            @RequestParam(value = "namePublisher", required = false) String namePublisher,
            @RequestParam(value = "addressPublisher", required = false) String addressPublisher,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Publisher> publishersPage = publisherService.getList(namePublisher,addressPublisher,page, size);

        if (publishersPage.isEmpty()) {
            return ResponseHandler.responeBuilder("No publisher found", HttpStatus.OK, false, null);
        } else {
            List<PublisherRespone> publisherRespones = publishersPage.getContent().stream()
                    .map(this::convertPublisherResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("categories", publisherRespones);
            response.put("currentPage", publishersPage.getNumber());
            response.put("totalItems", publishersPage.getTotalElements());
            response.put("totalPages", publishersPage.getTotalPages());
            //   response.put("currentPage", categoryPage.getNumber());

            return ResponseHandler.responeBuilder("Distributor found", HttpStatus.OK, true, response);
        }}
    private PublisherRespone convertPublisherResponse(Publisher publisher) {
        //  String baseUrl = "http://localhost:8080/category/image/";
        //  String imageUrl = baseUrl + encodeURIComponent(category.getImage());

        PublisherRespone response = new PublisherRespone();
        response.setIdPublisher(publisher.getIdPublisher());
        response.setNamePublisher(publisher.getNamePublisher());
        response.setAddressPublisher(publisher.getAddressPublisher());
        response.setPhonePublisher(publisher.getPhonePublisher());
      //  response.setAddressPublisher(distributor.getAddress());
        //   response.setImageUrl(imageUrl);
        response.setCreateAt(publisher.getCreateAt());
        response.setUpdateAt(publisher.getUpdateAt());
        response.setDeleted(publisher.isDeleted());
        return response;
    }

    @GetMapping("/{idPublisher}")
    public ResponseEntity<?> getPublisherDetails(@PathVariable("idPublisher") Integer idPublisher) {
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