package com.graduates.test.controller;

import com.graduates.test.dto.CategoryRespone;
import com.graduates.test.dto.DistributorRespone;
import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Category;
import com.graduates.test.model.Distributor;
import com.graduates.test.model.Publisher;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/distributor")
public class DistributorController {


    private DistributorService distributorService;
    @Autowired

    public DistributorController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createDistributorDetails(
            @RequestParam(value = "nameDistributor", required = true) String nameDistributor,
            @RequestParam(value = "address", required = true) String address,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "email", required = true) String email

    ) {

        try {
            // Tạo đối tượng Publisher
      Distributor  distributor = new Distributor();
            distributor.setNameDistributor(nameDistributor);
            distributor.setAddress(address);
            distributor.setPhone(phone);
            distributor.setEmail(email);
            // Tạo nhà xuất bản mới
            String result = distributorService.createDistributor(distributor);

            // Trả về phản hồi thành công với thông điệp từ service
            return ResponseHandler.responeBuilder(result, HttpStatus.OK, true, null);

        } catch (Exception e) {
            // Xử lý các lỗi không dự đoán trước
            return ResponseHandler.responeBuilder("An error occurred while creating the distributor", HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }
    @PutMapping("/{idDistributor}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateDistributorDetails(
            @PathVariable("idDistributor") Integer idDistributor,
            @RequestParam(value = "nameDistributor", required = true) String nameDistributor,
            @RequestParam(value = "address", required = true) String address,
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "email", required = true) String email
    ) {

        try {
            // Tìm kiếm và kiểm tra nhà xuất bản hiện tại
            Distributor existingPublisher = distributorService.findById(idDistributor);
            if (existingPublisher == null) {
                // Nhà xuất bản không tồn tại
                return ResponseHandler.responeBuilder("Publisher not found", HttpStatus.NOT_FOUND, false, null);
            }

            // Cập nhật các thông tin từ các tham số được cung cấp
            if (nameDistributor != null) existingPublisher.setNameDistributor(nameDistributor);
            if (address != null) existingPublisher.setAddress(address);
            if(phone!=null) existingPublisher.setPhone(phone);
            if(email!=null) existingPublisher.setEmail(email);
            // Gọi service để cập nhật nhà xuất bản
            String result = distributorService.updateDistributor(existingPublisher);

            // Trả về phản hồi thành công
            return ResponseHandler.responeBuilder(result, HttpStatus.OK, true, null);

        } catch (Exception e) {
            // Xử lý lỗi và trả về mã lỗi 500
            return ResponseHandler.responeBuilder("Error updating distributor", HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }

    @DeleteMapping("/{idDistributor}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteDistributor(@PathVariable Integer idDistributor) {
        try {
            distributorService.markDistributorAsDeleted(idDistributor);
            return ResponseHandler.responeBuilder("publisher deleted successfully.",HttpStatus.OK,true,null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    public ResponseEntity<?> deleteCategory(@PathVariable("idCategory") Integer idCategory) {
//        try {
//            categoryService.markCategoryAsDeleted(idCategory);
//            return ResponseHandler.responeBuilder("Category deleted successfully.", HttpStatus.OK, true, null);
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

//    @GetMapping("/list")
//    public ResponseEntity<List<Distributor>> getAllCategory() {
//        return ResponseEntity.ok(distributorService.getAllDistributor());
//    }


    @GetMapping("/list")
    public ResponseEntity<?> getDistributorList(
            @RequestParam(value = "nameDistributor", required = false) String nameDistributor,
           @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<Distributor> distributorsPage = distributorService.getList(nameDistributor,address,page, size);

        if (distributorsPage.isEmpty()) {
            return ResponseHandler.responeBuilder("No distributor found", HttpStatus.OK, false, null);
        } else {
            List<DistributorRespone> distributorRespones = distributorsPage.getContent().stream()
                    .map(this::convertDistributorResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("categories", distributorRespones);
            response.put("currentPage", distributorsPage.getNumber());
            response.put("totalItems", distributorsPage.getTotalElements());
            response.put("totalPages", distributorsPage.getTotalPages());
            //   response.put("currentPage", categoryPage.getNumber());

            return ResponseHandler.responeBuilder("Distributor found", HttpStatus.OK, true, response);
        }
    }


    private DistributorRespone convertDistributorResponse(Distributor distributor) {
      //  String baseUrl = "http://localhost:8080/category/image/";
      //  String imageUrl = baseUrl + encodeURIComponent(category.getImage());

        DistributorRespone response = new DistributorRespone();
        response.setIdDistributor(distributor.getIdDistributor());
        response.setNameDistributor(distributor.getNameDistributor());
        response.setAddress(distributor.getAddress());
        response.setPhone(distributor.getPhone());
        response.setAddress(distributor.getAddress());
     //   response.setImageUrl(imageUrl);
        response.setCreateAt(distributor.getCreateAt());
        response.setUpdateAt(distributor.getUpdateAt());
        response.setDeleted(distributor.isDeleted());
        return response;
    }

    @GetMapping("/{idPublisher}")
    public ResponseEntity<?> getDistributorDetails(@PathVariable("idDistributor") Integer idDistributor) {
        try {

            Distributor distributor= distributorService.getDistributor(idDistributor);

            // Trả về thông tin nhà xuất bản nếu tìm thấy
            return ResponseHandler.responeBuilder("success", HttpStatus.OK, true, distributor);

        } catch (ResourceNotFoundException e) {
            // Xử lý trường hợp nhà xuất bản không tồn tại
            return ResponseHandler.responeBuilder(e.getMessage(), HttpStatus.NOT_FOUND, false, null);

        } catch (Exception e) {
            // Xử lý các lỗi không dự đoán trước
            return ResponseHandler.responeBuilder("An error occurred while retrieving the publisher", HttpStatus.INTERNAL_SERVER_ERROR, false, null);
        }
    }

}
