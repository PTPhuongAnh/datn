package com.graduates.test.controller;

import com.graduates.test.exception.ResourceNotFoundException;
import com.graduates.test.model.Distributor;
import com.graduates.test.model.Publisher;
import com.graduates.test.response.ResponseHandler;
import com.graduates.test.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/distributor")
public class DistributorController {


    private DistributorService distributorService;
    @Autowired

    public DistributorController(DistributorService distributorService) {
        this.distributorService = distributorService;
    }

    @PostMapping("/create")
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
    public ResponseEntity<?> deleteDistributor(@PathVariable Integer idDistributor) {
        try {
            distributorService.deleteDistributor(idDistributor);
            return ResponseHandler.responeBuilder("publisher deleted successfully.",HttpStatus.OK,true,null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Distributor>> getAllCategory() {
        return ResponseEntity.ok(distributorService.getAllDistributor());
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
